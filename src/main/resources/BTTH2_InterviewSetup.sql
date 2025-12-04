-- =====================================================
-- BTTH2 - QUẢN LÝ ĐỀ TÀI - SQL CHO INTERVIEW
-- Phiên bản rút gọn: chỉ dùng cho bài test SQL/vibecoding
-- Bảng sử dụng:
--   - SINHVIEN
--   - DETAI
--   - SV_DETAI
-- =====================================================

-- Xóa bảng nếu đã tồn tại (theo thứ tự ngược để tránh lỗi FK)
DROP TABLE IF EXISTS SV_DETAI CASCADE;
DROP TABLE IF EXISTS DETAI CASCADE;
DROP TABLE IF EXISTS SINHVIEN CASCADE;

-- =====================================================
-- 1. TẠO BẢNG
-- =====================================================

-- Bảng: SINHVIEN
CREATE TABLE SINHVIEN (
    MSSV   CHAR(8)      PRIMARY KEY,
    TENSV  VARCHAR(30)  NOT NULL,
    SODT   VARCHAR(10)  NOT NULL,
    LOP    CHAR(10)     NOT NULL,
    DIACHI VARCHAR(50)
);

-- Bảng: DETAI
CREATE TABLE DETAI (
    MSDT  CHAR(6)      PRIMARY KEY,
    TENDT VARCHAR(30)  NOT NULL
);

-- Bảng: SV_DETAI (quan hệ sinh viên - đề tài)
CREATE TABLE SV_DETAI (
    MSSV CHAR(8),
    MSDT CHAR(6),
    PRIMARY KEY (MSSV, MSDT),
    FOREIGN KEY (MSSV) REFERENCES SINHVIEN(MSSV) ON DELETE CASCADE,
    FOREIGN KEY (MSDT) REFERENCES DETAI(MSDT) ON DELETE CASCADE
);

-- =====================================================
-- 2. INSERT DỮ LIỆU MẪU
-- (Giữ nguyên dữ liệu liên quan từ file BTTH2_DatabaseSetup gốc)
-- =====================================================

-- SINHVIEN
INSERT INTO SINHVIEN (MSSV, TENSV, SODT, LOP, DIACHI) VALUES
('13520001', 'Nguyễn Văn An',  '0906762255', 'SE103.U32', 'THỦ ĐỨC'),
('13520002', 'Phan Tấn Đạt',   '0975672350', 'IE204.T21', 'QUẬN 1'),
('13520003', 'Nguyễn Anh Hải', '0947578688', 'IE205.R12', 'QUẬN 9'),
('13520004', 'Phạm Tài',       '0956757869', 'IE202.A22', 'QUẬN 1'),
('13520005', 'Lê Thúy Hằng',   '0976668688', 'SE304.E22', 'THỦ ĐỨC'),
('13520006', 'Ưng Hồng Ân',    '0957475898', 'IE208.F33', 'QUẬN 2'),
-- Sinh viên mới, chưa có đề tài, dùng cho happy case assign
('13520007', 'Trần Minh Khang','0905123456', 'SE103.U32', 'QUẬN 3');

-- DETAI
INSERT INTO DETAI (MSDT, TENDT) VALUES
('97001', 'Quản lý thư viện'),
('97002', 'Nhận dạng vân tay'),
('97003', 'Bán đấu giá trên mạng'),
('97004', 'Quản lý siêu thị'),
('97005', 'Xử lý ảnh'),
('97006', 'Hệ giải toán thông minh'),
-- Đề tài mới, chưa có sinh viên, dùng cho happy case assign
('97007', 'Quản lý phòng gym');

-- SV_DETAI
INSERT INTO SV_DETAI (MSSV, MSDT) VALUES
('13520001', '97004'),
('13520002', '97005'),
('13520003', '97001'),
('13520004', '97002'),
('13520005', '97003'),
('13520006', '97005');

-- =====================================================
-- 3. GỢI Ý CÂU LỆNH VERIFY (TÙY CHẠY)
-- =====================================================

-- SELECT * FROM SINHVIEN;
-- SELECT * FROM DETAI;
-- SELECT * FROM SV_DETAI;

-- GỢI Ý CASE TEST CHO API
-- 1) API GET /api/detai/by-class
--    - Happy:  lop=SE103.U32  (có sinh viên 13520001, 13520007)
--    - Empty:  lop=NO_CLASS   (không tồn tại lớp -> trả về [])
--
-- 2) API POST /api/detai/{msdt}/assign  body: {"mssv": "..."}
--    - Happy:      msdt=97007, mssv=13520007  (đề tài & sinh viên đều chưa có quan hệ)
--    - 404 NotFound:  msdt=999999, mssv=13520001
--    - 400 BadRequest: msdt=97001,  mssv=99999999  (sinh viên không tồn tại)
--    - 409 Conflict (đề tài đã có SV khác):
--          msdt=97004, mssv=13520002  (97004 đã gán cho 13520001)
--    - 409 Conflict (SV đã có đề tài khác):
--          msdt=97006, mssv=13520001  (13520001 đã có đề tài 97004)
--    - 400 BadRequest (body thiếu mssv hoặc rỗng):
--          msdt=97007, body: {}
--          msdt=97007, body: {"mssv": ""}
--    - 409 Conflict (đề tài tự nhận lại chính sinh viên đang giữ):
--          msdt=97004, mssv=13520001  (nên trả 200 vì cùng sinh viên) -> dùng để kiểm tra logic duplicate
--    - Multi-step: 
--          1. POST msdt=97007, mssv=13520007  -> expect 200
--          2. GET  /api/detai/by-class?lop=SE103.U32 -> đề tài 97007 phải hiển thị mssv=13520007
--          3. POST msdt=97007, mssv=13520002 -> expect 409 (đề tài đã có sinh viên khác)


