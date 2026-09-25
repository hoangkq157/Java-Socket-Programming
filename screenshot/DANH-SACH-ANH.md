# Danh sách ảnh chụp màn hình cần nộp

Chụp bằng **Win + Shift + S** (Snipping Tool), lưu file **PNG** vào đúng thư mục
`screenshot/` này, đặt tên đúng như dưới đây để giáo viên dễ đối chiếu.

| # | Tên file | Nội dung cần chụp | Cách làm |
|---|---|---|---|
| 1 | `01-eclipse-project.png` | Cây project trong Eclipse hiện đủ 3 package tcp/udp/multicast (7 file .java) | Mở Eclipse, chụp cửa sổ Package Explorer |
| 2 | `02-tcp-client-code.png` | Code `EchoChatClient.java` đang mở trong Eclipse | Mở file, chụp vùng editor |
| 3 | `03-tcp-single-server-code.png` | Code `EchoChatSingleServer.java` đang mở | Mở file, chụp vùng editor |
| 4 | `04-tcp-single-console.png` | Console Server (`Client accepted: ...`) **và** Console Client (`0 1 2 ... 9`) | Chạy server + client, dùng "New Console View" để đặt 2 console cạnh nhau rồi chụp |
| 5 | `05-tcp-multi-server-code.png` | Code `EchoChatMultiServer.java` + `WorkerThread.java` | Mở file, chụp vùng editor (2 ảnh ghép hoặc chụp riêng) |
| 6 | `06-tcp-multi-console.png` | Console Multi Server (`Processing`/`Complete processing`) + 2 console client (mỗi cái `0 1 2 ... 9`) | Chạy multi server + client 2 lần ("Launch a New Launched Instance"), chụp |
| 7 | `07-udp-server-code.png` | Code `EchoServer.java` đang mở | Mở file, chụp vùng editor |
| 8 | `08-udp-client-code.png` | Code `EchoClient.java` đang mở | Mở file, chụp vùng editor |
| 9 | `09-udp-console.png` | Console UDP Server (`Received: gpcoder`) + Console Client (gõ `gpcoder` → `Received: REDOCPG`) | Chạy server + client, gõ vài tin nhắn, chụp 2 console |
| 10 | `10-multicast-code.png` | Code `MulticastSender.java` + `MulticastReceiver.java` | Mở file, chụp vùng editor |
| 11 | `11-multicast-console.png` | Console Receiver (`Received: Hello from multicast sender` nhiều dòng) + Console Sender (`Sent: ...`) | Chạy Receiver trước, Sender sau, đợi vài giây rồi chụp |
| 12 | `12-github-repo.png` | Trang repo GitHub sau khi push code lên (thấy đầy đủ src/, screenshot/, README) | Mở link repo trên trình duyệt, chụp |

## Ghi chú khi chụp

- Ảnh console nên chụp **cả thanh tiêu đề tab Console** để thấy tên lớp đang chạy.
- Trước khi chụp console server/client, cuộn console về cuối để kết quả dễ thấy.
- Nhớ **Terminate** các process cũ (nút vuông đỏ) trước khi chạy ví dụ mới, tránh
  lỗi `Address already in use`.
- Sau khi chụp đủ 12 ảnh, làm theo `HUONG-DAN-GIT-SOCKET.md` (ở thư mục học phần)
  để commit và push lên GitHub.
