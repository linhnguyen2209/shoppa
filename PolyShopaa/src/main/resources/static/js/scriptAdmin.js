
// ******* Giữ chỗ tab cho view management ********

document.addEventListener("DOMContentLoaded", function () {
    // Lấy tab hiện tại từ localStorage
    let activeTab = localStorage.getItem('activeTab');
    if (activeTab) {
        let tab = document.querySelector(`button[data-bs-target="${activeTab}"]`);
        if (tab) {
            tab.click();
            // Cuộn đến vị trí đã lưu
            let scrollPosition = localStorage.getItem('scrollPosition');
            if (scrollPosition) {
                window.scrollTo(0, parseInt(scrollPosition, 10));
            }
        }
    }

    // Lắng nghe sự kiện chuyển tab và lưu trạng thái tab vào localStorage
    let tabs = document.querySelectorAll('button[data-bs-toggle="tab"]');
    tabs.forEach(tab => {
        tab.addEventListener('shown.bs.tab', function (event) {
            let target = event.target.getAttribute('data-bs-target');
            localStorage.setItem('activeTab', target);
            // Lưu vị trí cuộn của trang
            localStorage.setItem('scrollPosition', window.scrollY);
        });
    });
});

// Lưu vị trí cuộn của trang khi người dùng di chuyển
window.addEventListener('scroll', function () {
    localStorage.setItem('scrollPosition', window.scrollY);
});

function openEditTab(tabId) {
    // Lưu tab và vị trí cuộn
    localStorage.setItem('activeTab', tabId);
    localStorage.setItem('scrollPosition', window.scrollY);

    // Mở tab tương ứng với tabId
    let tab = document.querySelector(`button[data-bs-target="${tabId}"]`);
    if (tab) {
        tab.click();
    }
}

// gửi form
function submitForm(action) {
    var form = document.getElementById('objectForm');
    form.action = action;
    form.submit();
}
