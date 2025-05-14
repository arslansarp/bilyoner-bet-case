Canlı Bahis Uygulaması - Case Study
Proje Tanımı
Bu proje, canlı bahis oranlarının gerçek zamanlı olarak güncellendiği bir bahis uygulamasının geliştirilmesini amaçlamaktadır. Uygulama, maç bilgilerini içeren bültenlerin oluşturulması, bültenin görüntülenmesi ve kullanıcıların bültende yer alan maçlara bahis yapabilmelerini sağlayacak REST API'lerin tasarım ve geliştirilmesini kapsar. Proje, Spring Boot 3.X, Java (17 veya 21) ve H2 Veritabanı kullanılarak hayata geçirilecektir.

Teknik Gereksinimler
1. REST API Tasarımı
A. Bülten Yönetimi
Amaç: Bahis yapılacak maçların bilgilerini içeren bültenlerin oluşturulması ve gerçek zamanlı olarak görüntülenmesini sağlamak.

Bülten Oluşturma API'si

İşlev: Yeni maç etkinliklerini (event) bülten içerisine eklemek.

Gereksinimler:

Her event, bir maçı temsil edecektir.

Aşağıdaki maç bilgileri zorunlu alanlar olarak eklenmelidir:

Lig Adı

Ev Sahibi Takım Adı

Deplasman Takım Adı

Ev Sahibi Kazanır Bahis Oranı (1.22, 1.85, 2.32 vb.)

Berabere Biter Bahis Oranı

Deplasman Takımı Kazanır Bahis Oranı

Karşılaşma Başlama Zamanı

Bülten Görüntüleme API'si

İşlev: Bültenin, bahis oranlarının saniyede bir kez rastgele güncellendiği şekilde görüntülenmesini sağlamak.

Özellikler:

Maçlar eklendikten sonra bahis oranları, belirlenen kurallar çerçevesinde rastgele güncellenecektir.

Bülten, tüm maçları ve oran değişikliklerini gerçek zamanlı olarak sunmalıdır.

B. Bahis İşlemleri
Amaç: Kullanıcının bülten üzerinden seçim yaparak bahis kuponu (betslip) oluşturabilmesini sağlamak.

Kupon Oluşturma API'si

İşlev: Kullanıcının, bültende listelenen maçlardan seçim yaparak bahis kuponu oluşturması.

Gereksinimler:

Giriş Parametreleri:

Event ID: Bahis yapılacak maçın benzersiz tanımlayıcısı.

Seçilen Bahis Türü: (Ev sahibi kazanır, berabere, deplasman kazanır vb.)

Customer ID: (Header üzerinden iletilecek)

Çoklu Kupon Adedi: Kaç adet oynanacağı belirler.

Bahis Yapılan Miktar

Fonksiyonellik:

Bahis oranlarının gerçek zamanlı olarak güncellendiğinden emin olunmalıdır.

Kullanıcının bahis yapmaya çalıştığı oran ile gerçekleşen oran arasında fark varsa, bu fark bildirilebilmelidir.

2. Kupon İşlemleri ve Zaman Aşımı Yönetimi
Çoklu Kupon Limiti:

Belirli bir maça maksimum 500 adet çoklu kupon yerleştirilebilir.

Oran Güncelleme İzolasyonu:

Bahis işlemi başlatıldığında alınan oran, işlem tamamlanana kadar korunmalıdır.

Timeout Yönetimi:

Her bir kupon için maksimum 2 saniye timeout süre uygulanmalıdır.

Bu süre parametrik olarak değiştirilebilir olmalıdır.

3. API Geliştirme ve Güvenlik
Tüm API'ler, RESTful servis prensiplerine uygun geliştirilmelidir.

Birim ve entegrasyon testleri, senaryoları doğrulamak için uygulanmalıdır.

Müşteri bilgileri ve güvenlik için basit bir mock yapı kullanılabilir.

Çoklu kupon yapılan bir kupon’unun kaç adet oynanacağını ifade eden bir alandır.

Gerçek dünyada kullanım amacı, bir kupona yapılabilen maksimum yatırım limitini aşmak için limit * çoklu kupon adedi kadar yatırım yapılabilmesini sağlar.

Bu case özelinde maksimum yatırım miktarı 10.000 TL olduğu varsayılabilir.

4. Kullanılacak Teknolojiler
Backend Framework: Spring Boot 3.X

Programlama Dili: Java 17 veya Java 21

Veritabanı: H2 DB
