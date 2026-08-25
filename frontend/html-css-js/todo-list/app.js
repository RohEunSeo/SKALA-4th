// HTML 요소 가져오기
const todoForm = document.querySelector("#todoForm");
const todoInput = document.querySelector("#todoInput");
const todoList = document.querySelector("#todoList");
// HTML에서 id로 요소를 찾아 변수에 저장

// 폼이 제출될 때 실행된다.
// 원래 문법이 요소.addEventListerner("이벤트이름", "실행할 함수)
// 괄호 안의 event는 이벤트 정보를 담은 객체
todoForm.addEventListener("submit", function (event) {
  // form 태그는 기본적으로 제출 시 화면을 새로고침한다.
  // TodoList에서는 새로고침이 필요 없으므로 기본 동작을 막는다.
  event.preventDefault(); // event 객체 안에 있는 메서드를 호출함

  // 입력값의 앞뒤 공백을 제거한다.
  // 입력창에 현재 입력된 텍스트 가져오기 -> .trim() -> 앞뒤 공백 제거, 스페이스만 입력했을 때 빈 항목 추가되는 것을 방지
  const todoText = todoInput.value.trim();

  // 입력값이 비어 있으면 경고창을 보여 주고 함수를 종료한다.
  if (todoText === "") {
    alert("할 일을 입력하세요.");
    todoInput.focus(); // 입력창에 커서를 두어서 다시 입력할 수 있게 함
    return;
  }

  // 입력한 내용을 목록에 추가한다.
  // addTodo(). 함수를 호출해서 목록에 추가. 
  addTodo(todoText);

  // 입력창을 비운다.
  todoInput.value = "";

  // 다시 입력할 수 있도록 입력창에 커서를 둔다.
  todoInput.focus();
});

// 할 일을 화면에 추가하는 함수
function addTodo(todoText) {
  // li 태그를 만든다.
  const todoItem = document.createElement("li");
  // 해당 문법으로 li 태그를 만들고 클래스 이름은 todo-item으로 저장한다.
  // CSS .todo-item{} 스타일이 적용된다
  todoItem.className = "todo-item";

  // 할 일 내용이 들어갈 span 태그를 만든다.
  const todoTextSpan = document.createElement("span");

  // textContent를 사용하면 입력한 문자를 그대로 표시한다.
  // 예: <h1>테스트</h1>를 입력해도 HTML 태그로 실행되지 않는다.
  todoTextSpan.textContent = todoText;

  // 버튼을 담을 div 태그를 만든다.
  // 사용자가 입력한 값을 표시할 땐 항상 textContent가 안전하다. 
  const actionBox = document.createElement("div");
  actionBox.className = "todo-actions";
  // JS로 HTML 태그를 코드로 만든 것
  // div 태그를 만들고 이름은 todo-actions으로 저장
  // 버튼들을 담는 박스를 actionBox라는 변수에 저장

  // 완료 버튼을 만든다.
  const doneButton = document.createElement("button");
  doneButton.type = "button";
  doneButton.className = "done-button";
  doneButton.textContent = "완료";

  // 삭제 버튼을 만든다.
  const deleteButton = document.createElement("button");
  deleteButton.type = "button";
  deleteButton.className = "delete-button";
  deleteButton.textContent = "삭제";

  // 완료 버튼을 클릭하면 done 클래스를 추가하거나 제거한다.
  doneButton.addEventListener("click", function () {
    todoItem.classList.toggle("done");
  });

  // 삭제 버튼을 클릭하면 현재 할 일 항목을 제거한다.
  deleteButton.addEventListener("click", function () {
    todoItem.remove();
  });
  // appendChild - 조립 순서
  // 이걸 실행해야 비로소 화면에 나타남 
  // 완료 버튼과 삭제 버튼을 actionBox 안에 넣는다.
  actionBox.appendChild(doneButton);
  actionBox.appendChild(deleteButton);

  // 할 일 내용과 버튼 영역을 li 안에 넣는다.
  todoItem.appendChild(todoTextSpan);
  todoItem.appendChild(actionBox);

  // 완성된 li를 ul 목록에 추가한다.
  todoList.appendChild(todoItem);
}