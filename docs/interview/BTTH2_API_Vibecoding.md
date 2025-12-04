## Đề bài Interview – API Vibecoding (BTTH2)

### 1. Bối cảnh

Hệ thống quản lý sinh viên và đề tài tốt nghiệp đang được xây dựng bằng **Spring Boot + H2 (in‑memory)**.  
Khi ứng dụng khởi động, file `BTTH2_InterviewSetup.sql` sẽ tự động:

- Tạo 3 bảng: `SINHVIEN`, `DETAI`, `SV_DETAI`.
- Insert sẵn một số dữ liệu mẫu.

Swagger UI đã được cấu hình sẵn, có thể mở tại:

- `http://localhost:8080/swagger-ui.html`  
  hoặc  
- `http://localhost:8080/swagger-ui/index.html`

### 2. Mục tiêu bài test

Trong vòng **~20 phút**, ứng viên cần:

- Sử dụng **Spring Boot, Spring Web, Spring Data JPA**.
- Dựa trên database có sẵn, **implement 2 API REST**:
  1. API lấy danh sách đề tài theo lớp.
  2. API để sinh viên nhận đề tài.
- Xuất API qua `@RestController` và **test được bằng Swagger UI** (hoặc Postman).

### 3. Mô hình dữ liệu (tóm tắt)

Ứng viên chỉ cần quan tâm đến 3 bảng:

```sql
-- Bảng sinh viên
SINHVIEN (
    MSSV   CHAR(8)       PRIMARY KEY,
    TENSV  VARCHAR(30)   NOT NULL,
    SODT   VARCHAR(10)   NOT NULL,
    LOP    CHAR(10)      NOT NULL,
    DIACHI VARCHAR(50)
);

-- Bảng đề tài
DETAI (
    MSDT  CHAR(6)      PRIMARY KEY,
    TENDT VARCHAR(30)  NOT NULL
);

-- Bảng quan hệ sinh viên – đề tài
SV_DETAI (
    MSSV CHAR(8),
    MSDT CHAR(6),
    PRIMARY KEY (MSSV, MSDT),
    FOREIGN KEY (MSSV) REFERENCES SINHVIEN(MSSV),
    FOREIGN KEY (MSDT) REFERENCES DETAI(MSDT)
);
```

Trong project đã có sẵn 2 entity (gợi ý):

- `SinhVien` map với bảng `SINHVIEN`.
- `DeTai` map với bảng `DETAI` và quan hệ nhiều‑nhiều qua `SV_DETAI`.

Ứng viên có thể dùng lại hoặc tự tạo entity/repository theo cách riêng, miễn là chạy đúng.

### 4. Yêu cầu API chi tiết

#### 4.1. API 1 – Lấy danh sách đề tài theo lớp

- **Method**: `GET`
- **Endpoint**: `/api/detai/by-class`
- **Query param**:
  - `lop` (bắt buộc), ví dụ: `SE103.U32`

**Mô tả nghiệp vụ**:

- Trả về danh sách tất cả đề tài trong hệ thống, kèm theo thông tin sinh viên của lớp đó (nếu có).
- Mỗi phần tử trong danh sách gồm các trường JSON:
  - `msdt`: mã số đề tài.
  - `tendt`: tên đề tài.
  - `mssv`: mã số sinh viên đang làm đề tài (có thể null).
  - `tensv`: tên sinh viên (có thể null).
  - `lop`: lớp của sinh viên (có thể null).
- Nếu đề tài chưa có sinh viên nhận:
  - `mssv`, `tensv`, `lop` = `null`, nhưng đề tài vẫn xuất hiện.
- Nếu lớp không tồn tại hoặc chưa có sinh viên nào → trả về **danh sách rỗng**, không lỗi.
- Sắp xếp kết quả theo `msdt` tăng dần.

**Yêu cầu kỹ thuật**:

- Tạo DTO response rõ ràng (không trả trực tiếp entity).
- Dùng `@GetMapping` và bind query param bằng `@RequestParam`.
- Xử lý logic trong Service (Controller chỉ gọi Service).

---

#### 4.2. API 2 – Sinh viên nhận đề tài

- **Method**: `POST`
- **Endpoint**: `/api/detai/{msdt}/assign`
- **Path variable**:
  - `msdt`: mã số đề tài.
- **Request body (JSON)**:

```json
{
  "mssv": "13520001"
}
```

**Nghiệp vụ bắt buộc**:

1. Nếu đề tài với `{msdt}` **không tồn tại** → trả về **HTTP 404 Not Found**.
2. Nếu sinh viên với `mssv` trong body **không tồn tại** → trả về **HTTP 400 Bad Request**.
3. Nếu đề tài đã có sinh viên khác nhận:
   - Trả về **HTTP 409 Conflict**.
   - Thông báo: ví dụ `"Đề tài đã có sinh viên nhận"`.
4. Nếu sinh viên đã nhận một đề tài khác (tồn tại bản ghi khác trong `SV_DETAI` với cùng `mssv`):
   - Trả về **HTTP 409 Conflict**.
   - Thông báo: ví dụ `"Sinh viên đã có đề tài"`.
5. Nếu hợp lệ:
   - Gán sinh viên vào đề tài (thêm bản ghi vào `SV_DETAI` hoặc cập nhật quan hệ).
   - Trả về thông tin đề tài sau khi gán, với cấu trúc JSON tương tự 1 phần tử của API 1:
     - `msdt`, `tendt`, `mssv`, `tensv`, `lop`.

**Yêu cầu kỹ thuật**:

- Tạo DTO request để nhận `mssv`.
- Sử dụng Service để xử lý toàn bộ business logic.
- Sử dụng các HTTP status code phù hợp: `200/201`, `400`, `404`, `409`.
- Có thể dùng `@Transactional` cho hàm service gán đề tài (nếu kịp).

---

### 5. Kỳ vọng tối thiểu trong buổi interview

Trong ~30 phút, **không bắt buộc hoàn hảo**, nhưng interviewer sẽ quan sát:

- Ứng viên:
  - Biết cách tạo `@RestController` và mapping endpoint.
  - Biết inject `Repository` hoặc `Service` bằng `@Autowired` / constructor.
  - Biết viết query JPA hoặc tự định nghĩa method trong `JpaRepository`.
  - Biết xử lý các case lỗi cơ bản và trả về HTTP status hợp lý.
- Có thể test được API qua Swagger UI hoặc Postman:
  - Gọi `GET /api/detai/by-class?lop=...`
  - Gọi `POST /api/detai/{msdt}/assign` với JSON body.

### 6. Gợi ý cấu trúc code (tham khảo – không bắt buộc)

- Package đề xuất:
  - `entity` – chứa `SinhVien`, `DeTai`.
  - `repository` – chứa `SinhVienRepository`, `DeTaiRepository`.
  - `service` – chứa `DeTaiService`.
  - `controller` – chứa `DeTaiController`.
  - `dto` hoặc `model` – chứa request/response DTO cho API.

Ứng viên có thể chọn cách tổ chức khác nếu muốn, miễn là:

- Code dễ đọc, dễ hiểu.
- API chạy đúng theo yêu cầu đề bài.


