<img src="./public/banner.png">

<p align="justify">MyKatalogue adalah aplikasi pencatatan katalog produk berbasis terminal (CLI) yang ditulis menggunakan Java. Aplikasi ini dirancang agar kita bisa mengelola inventaris barang dengan lebih rapi; mulai dari menambah produk baru, mengurutkan daftar barang, hingga mencari produk secara spesifik lewat kata kunci. Keunggulan utama dari MyKatalogue adalah penyimpanan datanya yang praktis menggunakan JSON, serta adanya fitur Spam Filter otomatis yang akan menyensor kata-kata terlarang secara real-time. Dengan begitu, setiap data deskripsi produk yang tersimpan dipastikan akan selalu aman dari teks yang tidak pantas.</p>

## Features

- **Penyimpanan Persisten (JSON)**: Menyimpan data produk secara permanen ke dalam berkas `products.json` untuk mencegah kehilangan data saat program dihentikan.
- **Pengurutan Terstruktur (Sorting)**: Menyediakan mekanisme pengurutan katalog berdasarkan kriteria spesifik, meliputi *rating*, harga, dan urutan alfabetis.
- **Pencarian Terindeks (Searching)**: Mengimplementasikan pencarian data produk yang efisien berdasarkan pencocokan kategori maupun kata kunci (*keyword*).
- **Sensor Teks Otomatis (Spam Filter)**: Mendeteksi dan menyensor kata-kata terlarang pada input pengguna secara *real-time*, disertai kemampuan pengelolaan daftar leksikon secara dinamis.

## Data Structure & Algorithm
Aplikasi ini diimplementasikan menggunakan serangkaian struktur data dan algoritma komputasional untuk mengoptimasi efisiensi eksekusi program:

1. **Struktur Data HashMap (`categoryIndex` & `keywordIndex`)**
   - **Fungsi**: Diaplikasikan sebagai mekanisme *indexing* data untuk memetakan kategori dan kata kunci ke referensi objek produk.
   - **Rasionalisasi**: Pemilihan *HashMap* didasarkan pada kompleksitas waktu pencarian (*lookup*) rata-rata asimptotik sebesar **O(1)**. Pendekatan ini secara signifikan meminimalisasi latensi kueri pencarian dengan mengeliminasi kebutuhan iterasi sekuensial (*linear search* dengan kompleksitas **O(n)**) di seluruh entri korpus data.

2. **Struktur Data HashSet (`forbiddenWords`)**
   - **Fungsi**: Diutilisasi pada modul `WordFilter` untuk mengelola himpunan leksikon terlarang (*forbidden words*).
   - **Rasionalisasi**: Implementasi antarmuka *Set* menjamin properti keunikan elemen tanpa memerlukan komputasi tambahan. Pengecekan eksistensi elemen dalam ruang *hash* dieksekusi secara konstan dalam waktu **O(1)**, sehingga memastikan stabilitas *Spam Filter* pada operasi repetitif.

3. **Algoritma Merge Sort (`SortingHelper`)**
   - **Fungsi**: Diimplementasikan sebagai utilitas pengurutan utama untuk memproses entri katalog berdasarkan parameter kuantitatif (harga dan *rating*) maupun leksikografis (abjad nama produk).
   - **Rasionalisasi**: *Merge Sort* dikategorikan sebagai *stable sort* yang proporsional untuk struktur objek kompleks. Algoritma ini memberikan jaminan matematis kompleksitas waktu **O(n log n)** pada keseluruhan skenario komputasi (*best-case, average-case,* maupun *worst-case*), menghindari degradasi performa yang mungkin terjadi pada koleksi objek berskala masif.

4. **Regular Expression (`Pattern` & `Matcher`)**
   - **Fungsi**: Diintegrasikan dalam modul pengolahan string untuk proses identifikasi dan substitusi leksikon terlarang pada spesifikasi dan deskripsi produk.
   - **Rasionalisasi**: Ekspresi reguler memfasilitasi pencocokan *string* komposit secara dinamis secara efisien. Pemanfaatan representasi token batas leksikal (*word boundaries* / `\b`) mendikotomi identifikasi hanya pada kata utuh, meminimalisasi tingkat probabilitas *false positives* (kesalahan sensor) pada *substring* parsial.

## Installation & Usage

### Prerequisites
Ensure you have **Java Development Kit (JDK)** 8 or higher installed:
```bash
java -version
```

### Setup & Run
1. Navigate to the project directory in your terminal:
   ```bash
   cd path/to/MyKatalogue
   ```
2. Compile the Java source files:
   ```bash
   javac *.java
   ```
3. Run the application:
   ```bash
   java Main
   ```

## Contributors
L0125066 - Satrio Ananda Widiyanto 
L0125103 - Ikhsan Raditya Purnawarman 
L0125123 - Reditya Aflah Fadhali 