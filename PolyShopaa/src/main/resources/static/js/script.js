
document.addEventListener("DOMContentLoaded", function () {
    function startFlashSaleTimer() {
        var endTime = new Date().getTime() + 24 * 60 * 60 * 1000; // Đếm ngược 24 giờ từ bây giờ
        var timer = setInterval(function () {
            var now = new Date().getTime();
            var distance = endTime - now;

            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);

            document.querySelector(".flash-sale__time--hours").innerText = ("0" + hours).slice(-2);
            document.querySelector(".flash-sale__time--minutes").innerText = ("0" + minutes).slice(-2);
            document.querySelector(".flash-sale__time--seconds").innerText = ("0" + seconds).slice(-2);

            if (distance < 0) {
                clearInterval(timer);
                document.querySelector(".flash-sale__time--hours").innerText = "00";
                document.querySelector(".flash-sale__time--minutes").innerText = "00";
                document.querySelector(".flash-sale__time--seconds").innerText = "00";
            }
        }, 1000);
    }

    startFlashSaleTimer();
});
