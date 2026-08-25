StockTrading2 (day3 실습과제4) — CRUD 8종 + Analysis 5종 구현
Context
/Users/roh-eunseo/SKALA/code/backend/0730/StockTrading2는 day2(StockTrading)와 동일한 골격에서, 실습을 위해 일부 메서드/클래스가 의도적으로 비워진 스켈레톤이다(// 교육생 실습을 위해 삭제되었습니다 주석이 표시된 파일들). build/, bin/ 폴더에 교수님이 컴파일해두신 참조 구현의 .class 파일이 남아있어 javap으로 원래 메서드 시그니처를 확인했는데, 참조 구현은 MyBatis(StockMapper) + REQUIRES_NEW 트랜잭션 감사로그(TradeAuditLog/TradeAuditService)까지 포함한 복잡한 버전이었다. 하지만 사용자가 명시적으로 "JPA 방식으로, 필요 시 @Query 사용"을 요구했으므로, MyBatis·감사로그 관련 부분은 구현하지 않고 순수 JPA(+ 필요한 곳만 @Query)로 13개 요구 기능만 구현한다. TradeAuditLog 엔티티/레포지토리는 스켈레톤에 이미 존재하지만 이번 요구사항에 없으므로 손대지 않는다.

확정된 설계 결정:

삭제 정책(User/Stock 공통): 이전 day2 프로젝트에서와 동일하게, Portfolio/Transaction에 참조되어 있으면 삭제 차단(예외 발생) — FK 무결성 보호 목적.
Stock PUT(수정): code는 불변 키로 취급, name/currentPrice/previousPrice만 갱신.
총수익률 공식(사용자 확정): 수익률(%) = (평가손익 합계 / 매수원금 합계) × 100 — 즉 보유 중인 종목만 대상으로 한 미실현 수익률. 초기 예치금 스키마 변경 없이 계산 가능.
Analysis 컨트롤러의 13번 항목은 기존 Portfolio/Transaction 서비스 로직을 재사용(위임)하고, 45번(총자산/총수익률)만 신규 @Query 집계 로직을 추가한다 — 코드 중복 방지.
변경 파일 상세
1. entity — 변경 없음 (User/Stock/Portfolio/Transaction 모두 완성 상태)
2. repository/PortfolioRepository.java
boolean existsByUserId(Long userId);
boolean existsByStockId(Long stockId);

@Query("SELECT COALESCE(SUM(p.quantity * s.currentPrice), 0) FROM Portfolio p JOIN p.stock s WHERE p.user.id = :userId")
Long sumCurrentValueByUserId(@Param("userId") Long userId);

@Query("SELECT COALESCE(SUM(p.quantity * p.averagePrice), 0) FROM Portfolio p WHERE p.user.id = :userId")
Long sumCostBasisByUserId(@Param("userId") Long userId);
뒤 2개가 "필요 시 @Query"에 해당하는 부분 — 단순 메서드 이름 파생으로는 표현 불가능한 수량 × 단가의 SUM 집계.

3. repository/TransactionRepository.java
boolean existsByUserId(Long userId);
boolean existsByStockId(Long stockId);
(findByUserIdAndStockIdOrderByTransactionDateDesc는 이미 존재 — Analysis #3에서 그대로 재사용)

4. service/UserService.java (+ controller/UserController.java)
deleteUser(Long id): 사용자 조회 → portfolioRepository.existsByUserId/transactionRepository.existsByUserId 체크 → 있으면 예외, 없으면 삭제. PortfolioRepository, TransactionRepository 필드 추가 필요.
getAllUsers(): userRepository.findAll() → DTO 변환 (day2와 동일 패턴)
Controller: DELETE /api/users/{id} (204), GET /api/users (전체 목록)
5. service/StockService.java (+ controller/StockController.java)
updateStock(Long id, StockDto dto): 조회 → name/currentPrice/previousPrice 갱신(code는 무시) → save
deleteStock(Long id): 조회 → portfolioRepository.existsByStockId/transactionRepository.existsByStockId 체크 → 차단/삭제. PortfolioRepository, TransactionRepository 필드 추가 필요.
getStockByCode(String code): stockRepository.findByCode(code) (이미 존재하는 레포지토리 메서드 활용)
Controller: PUT /api/stocks/{id}, DELETE /api/stocks/{id} (204), GET /api/stocks/code/{code}
6. service/PortfolioService.java (+ controller/PortfolioController.java)
주석 처리된 3개 메서드를 day2 프로젝트와 동일한 로직으로 복원: addToPortfolio(userId, stockId, quantity, price)(신규 매수 시 생성 or 기존 보유 시 평균단가 재계산), updatePortfolio(userId, stockId, newQuantity)(매도 후 잔여수량 갱신, 0 이하면 삭제), removeFromPortfolio(userId, stockId)
getPortfolio(userId, stockId): portfolioRepository.findByUserIdAndStockId 활용 (요구사항 8번)
UserRepository, StockRepository 필드 추가 필요(매수 시 User/Stock 엔티티 조회용)
Controller: GET /api/portfolios/user/{userId}/stock/{stockId}
7. service/TransactionService.java (+ controller/TransactionController.java)
executeTrade(TradeRequestDto): day2의 TransactionService.executeTrade와 동일 로직 — User/Stock 조회 → BUY면 잔액검증 후 차감 + portfolioService.addToPortfolio(...), SELL이면 보유수량 검증 후 잔액증가 + portfolioService.updatePortfolio/removeFromPortfolio → Transaction 저장. UserRepository, StockRepository, PortfolioService 필드 추가 필요.
getTransactionById(Long id): 조회 실패 시 예외. 요구사항에 "Read-Only" 명시되어 있으므로 기존 getUserTransactions와 동일하게 @Transactional(readOnly = true, propagation = Propagation.SUPPORTS) 적용(이 파일에 이미 쓰인 컨벤션).
getUserStockTransactions(Long userId, Long stockId): 이미 있는 findByUserIdAndStockIdOrderByTransactionDateDesc 활용 (Analysis #3에서도 재사용)
Controller: POST /api/transactions/trade (201), GET /api/transactions/{id}
8. service/StockAnalysisService.java + controller/StockAnalysisController.java (현재 빈 스텁 → 새로 작성)
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockAnalysisService {
    private final PortfolioService portfolioService;
    private final TransactionService transactionService;
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public List<PortfolioDto> getPortfolioWithProfitLoss(Long userId) {
        return portfolioService.getUserPortfolio(userId);   // 위임 (중복 로직 없음)
    }

    public List<TransactionDto> getTransactionsWithDetails(Long userId) {
        return transactionService.getUserTransactions(userId);   // 위임
    }

    public List<TransactionDto> getStockTransactionsWithDetails(Long userId, Long stockId) {
        return transactionService.getUserStockTransactions(userId, stockId);   // 위임
    }

    public Long getTotalAssets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(...);
        Long portfolioValue = portfolioRepository.sumCurrentValueByUserId(userId);
        return user.getBalance() + portfolioValue;
    }

    public Double getTotalReturnRate(Long userId) {
        Long costBasis = portfolioRepository.sumCostBasisByUserId(userId);
        Long currentValue = portfolioRepository.sumCurrentValueByUserId(userId);
        if (costBasis == 0) return 0.0;
        return (currentValue - costBasis) * 100.0 / costBasis;
    }
}
Controller (@RequestMapping("/api/analysis")):

GET /api/analysis/portfolio-profit-loss/{userId} → List<PortfolioDto>
GET /api/analysis/transactions/{userId} → List<TransactionDto>
GET /api/analysis/transactions/{userId}/stock/{stockId} → List<TransactionDto>
GET /api/analysis/total-assets/{userId} → Long
GET /api/analysis/total-return-rate/{userId} → Double
9. src/main/resources/data.sql
현재 주식 5건 → 요구사항(10건 이상) 충족을 위해 5건 추가(기아 000270, 현대차 005380, KB금융 105560, 신한지주 055550, 현대모비스 012330), 총 10건.

검증 방법
./gradlew bootRun (포트 충돌 시 8080 사용중인 기존 프로세스 확인 후 처리 — 지난번처럼 이미 떠 있는 프로세스가 있을 수 있음)
Swagger UI(/swagger-ui.html)에서 13개 엔드포인트 각각 정상 케이스 + 에러 케이스 curl로 확인:
매수(POST /trade) → 포트폴리오 반영 확인 → Analysis 총자산/총수익률이 매수 전후로 올바르게 변하는지 확인
User/Stock 삭제: 거래 없는 대상은 204, 거래 있는 대상은 차단 확인
GET /api/stocks/code/{code}, GET /api/transactions/{id}, GET /api/portfolios/user/{userId}/stock/{stockId} 단건 조회 확인
data.sql 재시작 후 GET /api/stocks 결과가 10건인지 확인