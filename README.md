# Bài tập Lập trình Socket Client-Server với Java

Bài làm theo bài viết: **[Xây dựng ứng dụng Client-Server với Socket trong Java](https://gpcoder.com/3679-xay-dung-ung-dung-client-server-voi-socket-trong-java/)** (gpcoder.com)

- Người thực hiện: `hoangkq157`
- Môi trường: Windows 10, Eclipse IDE for Java Developers (2026-09), JDK 25 (Temurin 25.0.4 đi kèm Eclipse)
- Code các ví dụ được **giữ nguyên 100% theo bài viết gốc** (package `com.gpcoder.*`)

---

## 1. Lý thuyết cơ bản

### 1.1. Socket là gì?

**Socket** là một điểm cuối (endpoint) của liên kết giao tiếp hai chiều giữa hai chương
trình chạy trên mạng. Mỗi socket được định danh bởi cặp **địa chỉ IP + cổng (port)**.

Trong mô hình **Client-Server**:

- **Server**: mở cổng và **chờ** kết nối (`ServerSocket.accept()` với TCP, `receive()` với UDP).
- **Client**: **chủ động** kết nối tới địa chỉ IP + port của server (`Socket` với TCP, `send()` với UDP).

Java hỗ trợ lập trình socket qua gói `java.net`:

| Lớp | Dùng cho |
|---|---|
| `Socket`, `ServerSocket` | TCP |
| `DatagramSocket`, `DatagramPacket` | UDP |
| `MulticastSocket` | Multicast (mở rộng của UDP) |

### 1.2. So sánh TCP / UDP / Multicast

| Tiêu chí | TCP | UDP | Multicast |
|---|---|---|---|
| Kiểu giao tiếp | 1–1 (unicast) | 1–1 (unicast) | 1–nhiều (group) |
| Kết nối | Có (handshake 3 bước) | Không | Không |
| Đảm bảo dữ liệu | Có (thứ tự, không mất) | Không | Không |
| Tốc độ | Chậm hơn | Nhanh | Nhanh, tiết kiệm băng thông |
| Ứng dụng điển hình | Web, chat, file transfer | DNS, game, streaming | IPTV, gửi broadcast nội bộ |

---

## 2. Cấu trúc project

```
JavaSocketProgramming/
├── src/
│   └── com/gpcoder/
│       ├── tcp/
│       │   ├── EchoChatClient.java        # Client TCP gửi '0'..'9'
│       │   ├── EchoChatSingleServer.java  # Server TCP tuần tự (1 client / lần)
│       │   ├── EchoChatMultiServer.java   # Server TCP đa luồng (thread pool)
│       │   └── WorkerThread.java          # Luồng xử lý từng client
│       ├── udp/
│       │   ├── EchoServer.java            # Server UDP đảo ngược + in hoa chuỗi
│       │   └── EchoClient.java            # Client UDP nhập tin nhắn từ bàn phím
│       └── multicast/
│           ├── MulticastSender.java       # Gói tin tới nhóm 224.0.0.1:8888
│           └── MulticastReceiver.java     # Tham gia nhóm và nhận gói tin
├── screenshot/                            # Ảnh chụp màn hình code + kết quả chạy
├── .gitignore
└── README.md
```

---

## 3. Ví dụ 1 — TCP: Echo Chat với Server tuần tự

**File**: `EchoChatClient.java` + `EchoChatSingleServer.java` — cổng **7**

**Cách hoạt động:**
- Server: `new ServerSocket(7)` → vòng lặp `accept()` chờ client. Với mỗi client,
  server đọc từng byte (`is.read()`) và viết trả lại (`os.write()`) — xử lý **một client
  tại một thời điểm**, client tiếp theo phải chờ.
- Client: kết nối `127.0.0.1:7`, gửi lần lượt ký tự `'0'` → `'9'`, mỗi ký tự chờ đọc
  kết quả echo rồi ngủ 200ms.

**Kết quả chạy thực tế (đã kiểm chứng trên máy, JDK 25):**

Console Server:
```
Binding to port 7, please wait  ...
Server started: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=7]
Waiting for a client ...
Client accepted: Socket[addr=/127.0.0.1,port=57711,localport=7]
Client accepted: Socket[addr=/127.0.0.1,port=57712,localport=7]
```

Console Client (lần 1 và lần 2 cho kết quả giống nhau):
```
Connected: Socket[addr=/127.0.0.1,port=7,localport=57711]
0 1 2 3 4 5 6 7 8 9
```

---

## 4. Ví dụ 2 — TCP: Server đa luồng (Multi-thread)

**File**: `EchoChatMultiServer.java` + `WorkerThread.java` — cổng **7**, thread pool **4**

**Cách hoạt động:**
- Server tạo `ExecutorService` với 4 luồng (`Executors.newFixedThreadPool(4)`).
- Mỗi client được `accept()` sẽ giao cho một `WorkerThread` xử lý → **nhiều client
  được phục vụ đồng thời**, server vẫn miễn cưỡng chờ client mới.

**Kết quả chạy thực tế (2 client chạy đồng thời):**

Console Server — thấy 2 client được xử lý song song (`Processing` cả 2 rồi mới `Complete` cả 2):
```
Binding to port 7, please wait  ...
Server started: ServerSocket[addr=0.0.0.0/0.0.0.0,localport=7]
Waiting for a client ...
Client accepted: Socket[addr=/127.0.0.1,port=57719,localport=7]
Client accepted: Socket[addr=/127.0.0.1,port=57720,localport=7]
Processing: Socket[addr=/127.0.0.1,port=57719,localport=7]
Processing: Socket[addr=/127.0.0.1,port=57720,localport=7]
Complete processing: Socket[addr=/127.0.0.1,port=57719,localport=7]
Complete processing: Socket[addr=/127.0.0.1,port=57720,localport=7]
```

2 Console Client đều in: `0 1 2 3 4 5 6 7 8 9`

> So sánh với Ví dụ 1: server tuần tự phải xong client 1 mới tới client 2;
> server đa luồng phục vụ cả 2 cùng lúc trong cùng khoảng 2 giây.

---

## 5. Ví dụ 3 — UDP: Echo Server / Client

**File**: `EchoServer.java` + `EchoClient.java` — cổng **7**

**Cách hoạt động:**
- Server: `DatagramSocket(7)`, nhận `DatagramPacket`, đảo ngược chuỗi
  (`StringBuilder.reverse()`) và in hoa (`toUpperCase()`) rồi gửi trả về đúng
  địa chỉ + port của client gói tin tới (`incoming.getAddress()`, `incoming.getPort()`).
- Client: nhập chuỗi từ bàn phím, đóng gói thành `DatagramPacket` gửi tới
  `127.0.0.1:7`, sau đó chờ nhận gói phản hồi. Không có kết nối — mỗi gói gửi
  là độc lập.

**Kết quả chạy thực tế:**

Console Server:
```
Server created on port 7
Waiting for messages from client ...
Received: gpcoder
Received: socket
```

Console Client:
```
Nhập tin nhắn:
gpcoder
Received: REDOCPG
Nhập tin nhắn:
socket
Received: TEKCOS
```

(`gpcoder` đảo ngược thành `redocpg`, in hoa thành `REDOCPG`)

> Lưu ý: code gốc của client nhận dữ liệu bằng `new String(receivePacket.getData())`
> — lấy cả bộ đệm 1000 byte nên sau chuỗi có thể thấy khoảng trắng thừa. Đây là
> hành vi của code mẫu trong bài viết, giữ nguyên theo yêu cầu.

---

## 6. Ví dụ 4 — Multicast: Sender / Receiver

**File**: `MulticastSender.java` + `MulticastReceiver.java` — nhóm `224.0.0.1`, cổng **8888**

**Cách hoạt động:**
- Receiver: `MulticastSocket(8888)` + `joinGroup(224.0.0.1)` để **tham gia nhóm**
  multicast, sau đó `receive()` chờ gói tin.
- Sender: `MulticastSocket()` + `setTimeToLive(1)` (gói tin chỉ đi trong mạng LAN),
  mỗi 1 giây gửi 1 gói `"Hello from multicast sender"` tới nhóm.
- Chạy **Receiver trước, Sender sau** (UDP không lưu gói tin — nếu Receiver chưa
  tham gia nhóm thì gói gửi trước đó bị mất).

**Kết quả chạy thực tế:**

Console Receiver:
```
Multicast Receiver running at:/[0:0:0:0:0:0:0:0]:8888
Received: Hello from multicast sender
Received: Hello from multicast sender
Received: Hello from multicast sender
Received: Hello from multicast sender
Received: Hello from multicast sender
```

Console Sender:
```
Multicast Sender running at:/[0:0:0:0:0:0:0:0]:57278
Sent: Hello from multicast sender
Sent: Hello from multicast sender
Sent: Hello from multicast sender
Sent: Hello from multicast sender
Sent: Hello from multicast sender
```

> Trên JDK mới (14+), `joinGroup(InetAddress)` được đánh dấu **deprecated** —
> Eclipse sẽ hiện cảnh báo vàng nhưng **vẫn biên dịch và chạy bình thường**.
> Đây là code gốc của bài viết nên được giữ nguyên.

---

## 7. Hướng dẫn chạy trên Eclipse

### 7.1. Tạo project (chỉ làm lần đầu)

1. Mở Eclipse → **File → New → Java Project**
2. Project name: `JavaSocketProgramming`
3. **Bỏ chọn** "Use default location" → **Browse** tới `C:\Users\PC\Documents\JavaSocketProgramming` → **Finish**
4. Nếu Eclipse hỏi *"Create module-info.java"* → bấm **Don't Create**
5. Package Explorer sẽ hiện sẵn `src/com/gpcoder/tcp|udp|multicast` với 7 file .java

> Kiểm tra encoding hiển thị đúng tiếng Việt: chuột phải project → Properties →
> Resource → Text file encoding = **UTF-8**.

### 7.2. Chạy từng ví dụ (quy tắc chung: **Server chạy trước, Client chạy sau**)

Chạy một file: mở file → chuột phải vùng soạn thảo → **Run As → Java Application**.

**Ví dụ 1 (TCP tuần tự):**
1. Chạy `EchoChatSingleServer` → Console in `Waiting for a client ...`
2. Chạy `EchoChatClient` → Console client in `0 1 2 ... 9`
3. Chạy lại `EchoChatClient` lần 2 để thấy server nhận client nối tiếp
4. Xong: bấm nút **Terminate** (vuông đỏ) để tắt server

**Ví dụ 2 (TCP đa luồng):** như trên nhưng chạy `EchoChatMultiServer`;
quan trọng: khi chạy client lần 2, ở hộp thoại "*Java Application* Already Exists"
chọn **"Launch a New Launched Instance"** (chọn Continue sẽ không tạo process mới).

**Ví dụ 3 (UDP):**
1. Chạy `EchoServer`
2. Chạy `EchoClient` → gõ `gpcoder` → Enter → thấy `Received: REDOCPG` → gõ tiếp
   tin nhắn khác. Xong: **Terminate** client rồi server.

**Ví dụ 4 (Multicast):**
1. Chạy `MulticastReceiver` **trước** (in `Multicast Receiver running at:...`)
2. Chạy `MulticastSender` sau → Receiver bắt đầu in `Received: ...` mỗi giây
3. Xong: Terminate cả hai

### 7.3. Xem nhiều Console cùng lúc

Khi chạy nhiều chương trình, mỗi chương trình có một Console riêng. Trên thanh
công cụ của tab Console:
- Nút **"Display Selected Console"** (hình màn hình) để **chuyển** giữa các process,
- Hoặc nút **"New Console View"** để **mở thêm** cửa sổ Console thứ hai đặt cạnh
  nhau (khuyên dùng khi chụp ảnh server + client).

---

## 8. Lỗi thường gặp

| Hiện tượng | Nguyên nhân & cách xử lý |
|---|---|
| `Can't connect to server` (TCP client) | Server chưa chạy, hoặc đã bị Terminate → chạy server trước |
| `BindException: Address already in use` khi chạy server | Server cũ còn chiếm cổng → bấm **Terminate** (vuông đỏ) tất cả process trong Console trước khi chạy lại |
| Cảnh báo vàng `joinGroup(...) is deprecated` | Chỉ là cảnh báo trên JDK 14+, chương trình vẫn chạy bình thường |
| Multicast Receiver không nhận được gì | Chưa chạy Receiver trước khi chạy Sender → chạy lại đúng thứ tự |
| Console client "đứng" khi nhập UDP | Đang chờ gõ bàn phím — gõ tin nhắn rồi Enter |

---

## 9. Ảnh chụp màn hình

Toàn bộ ảnh chụp màn hình (code trong Eclipse + kết quả chạy) nằm trong thư mục
[`screenshot/`](screenshot/), danh sách chi tiết trong
[`screenshot/DANH-SACH-ANH.md`](screenshot/DANH-SACH-ANH.md).

---

*Nguồn tham khảo: [gpcoder.com — Xây dựng ứng dụng Client-Server với Socket trong Java](https://gpcoder.com/3679-xay-dung-ung-dung-client-server-voi-socket-trong-java/)*
