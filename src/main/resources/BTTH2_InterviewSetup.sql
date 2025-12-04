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
('13520006', 'Ưng Hồng Ân',    '0957475898', 'IE208.F33', 'QUẬN 2');

-- DETAI
INSERT INTO DETAI (MSDT, TENDT) VALUES
('97001', 'Quản lý thư viện'),
('97002', 'Nhận dạng vân tay'),
('97003', 'Bán đấu giá trên mạng'),
('97004', 'Quản lý siêu thị'),
('97005', 'Xử lý ảnh'),
('97006', 'Hệ giải toán thông minh');

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


