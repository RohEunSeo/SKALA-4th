"""
프로젝트명: Web-WAS-DB 3-tier 미니 게시판
파일명   : app.py
작성자   : 노은서
작성일   : 2026-08-21
설명     : Flask 기반 WAS 서버.
           PostgreSQL과 연결하여 게시글 CRUD 및 검색 API 제공.
           환경변수로 DB 접속 정보를 외부에서 주입받음.
"""

import os
import psycopg2
from flask import Flask, jsonify, request
from flask_cors import CORS

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
CORS(app)


def get_db():
    """DB 연결 객체 반환. 접속 정보는 환경변수에서 읽어옴."""
    return psycopg2.connect(
        host=os.environ.get("DB_HOST"),
        database=os.environ.get("DB_NAME"),
        user=os.environ.get("DB_USER"),
        password=os.environ.get("DB_PASSWORD")
    )


def init_db():
    """앱 시작 시 posts 테이블 없으면 자동 생성."""
    try:
        conn = get_db()
        cur = conn.cursor()
        cur.execute("""
            CREATE TABLE IF NOT EXISTS posts (
                id SERIAL PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                content TEXT,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """)
        conn.commit()
        cur.close()
        conn.close()
        print("[DB 초기화 성공]", flush=True)
    except Exception as e:
        print(f"[DB 연결 실패] {e}", flush=True)


def validate_content(data):
    """입력값 검증. 비어있거나 500자 초과 시 에러 반환."""
    if not data:
        return None, "요청 데이터가 없습니다."
    content = data.get("content", "").strip()
    if not content:
        return None, "내용을 입력해주세요."
    if len(content) > 500:
        return None, "내용은 500자 이하로 입력해주세요."
    return content, None


@app.route('/')
def index():
    """서버 상태 확인용 헬스체크 엔드포인트."""
    return jsonify({"message": "WAS 서버 정상 동작 중!"})


@app.route('/posts', methods=['GET'])
def get_posts():
    """
    게시글 목록 조회 + 검색.
    ?q=키워드 파라미터로 검색 가능. 없으면 전체 조회.
    """
    try:
        keyword = request.args.get('q', '').strip()
        conn = get_db()
        cur = conn.cursor()
        if keyword:
            # SQL 인젝션 방지를 위해 파라미터 바인딩 사용
            cur.execute("""
                SELECT id, title, content, created_at FROM posts
                WHERE title ILIKE %s OR content ILIKE %s
                ORDER BY id DESC
            """, (f"%{keyword}%", f"%{keyword}%"))
        else:
            cur.execute("SELECT id, title, content, created_at FROM posts ORDER BY id DESC")
        rows = cur.fetchall()
        cur.close()
        conn.close()
        posts = [{"id": r[0], "title": r[1], "content": r[2], "created_at": str(r[3])} for r in rows]
        return jsonify(posts)
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/posts', methods=['POST'])
def create_post():
    """새 게시글 작성. 성공 시 201 반환."""
    try:
        content, err = validate_content(request.json)
        if err:
            return jsonify({"error": err}), 400
        title = request.json.get("title", "").strip() or "제목 없음"
        conn = get_db()
        cur = conn.cursor()
        cur.execute(
            "INSERT INTO posts (title, content) VALUES (%s, %s) RETURNING id",
            (title, content)
        )
        new_id = cur.fetchone()[0]
        conn.commit()
        cur.close()
        conn.close()
        return jsonify({"message": "게시글 작성 완료", "id": new_id}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/posts/<int:post_id>', methods=['PUT'])
def update_post(post_id):
    """게시글 수정. 없는 게시글이면 404 반환."""
    try:
        content, err = validate_content(request.json)
        if err:
            return jsonify({"error": err}), 400
        conn = get_db()
        cur = conn.cursor()
        cur.execute(
            "UPDATE posts SET content=%s WHERE id=%s RETURNING id",
            (content, post_id)
        )
        updated = cur.fetchone()
        conn.commit()
        cur.close()
        conn.close()
        if not updated:
            return jsonify({"error": "존재하지 않는 게시글입니다."}), 404
        return jsonify({"message": "게시글 수정 완료"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/posts/<int:post_id>', methods=['DELETE'])
def delete_post(post_id):
    """게시글 삭제. 없는 게시글이면 404 반환."""
    try:
        conn = get_db()
        cur = conn.cursor()
        cur.execute("DELETE FROM posts WHERE id=%s RETURNING id", (post_id,))
        deleted = cur.fetchone()
        conn.commit()
        cur.close()
        conn.close()
        if not deleted:
            return jsonify({"error": "존재하지 않는 게시글입니다."}), 404
        return jsonify({"message": "게시글 삭제 완료"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == '__main__':
    init_db()                           # 서버 시작 전 DB 테이블 초기화
    app.run(host='0.0.0.0', port=5000)  # 0.0.0.0: 컨테이너 외부에서도 접근 가능