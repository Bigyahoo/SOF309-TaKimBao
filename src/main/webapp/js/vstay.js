/**
 * Vstay.vn - Core Client Script
 */

document.addEventListener("DOMContentLoaded", () => {
    // 1. Dark Mode Toggle
    const btnDarkMode = document.getElementById("btnDarkMode");
    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark-mode");
        if (btnDarkMode) btnDarkMode.textContent = "☀️";
    }

    if (btnDarkMode) {
        btnDarkMode.addEventListener("click", () => {
            document.body.classList.toggle("dark-mode");
            const isDark = document.body.classList.contains("dark-mode");
            btnDarkMode.textContent = isDark ? "☀️" : "🌙";
            localStorage.setItem("theme", isDark ? "dark" : "light");
        });
    }

    // 2. Back to Top Button
    const btnBackToTop = document.getElementById("btnBackToTop");
    if (btnBackToTop) {
        btnBackToTop.addEventListener("click", () => {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        });
    }

    // 3. Flash Sale Countdown Timer
    const hoursEl = document.getElementById("flashHours");
    const minutesEl = document.getElementById("flashMinutes");
    const secondsEl = document.getElementById("flashSeconds");

    if (hoursEl && minutesEl && secondsEl) {
        let flashTimeInSeconds = (8 * 3600) + (45 * 60) + 12;
        setInterval(() => {
            if (flashTimeInSeconds > 0) {
                flashTimeInSeconds--;
                const h = Math.floor(flashTimeInSeconds / 3600);
                const m = Math.floor((flashTimeInSeconds % 3600) / 60);
                const s = flashTimeInSeconds % 60;

                hoursEl.textContent = String(h).padStart(2, "0");
                minutesEl.textContent = String(m).padStart(2, "0");
                secondsEl.textContent = String(s).padStart(2, "0");
            }
        }, 1000);
    }

    // 4. Section Collapsible Toggles
    const toggleDestinationsBtn = document.getElementById("toggleDestinationsBtn");
    const destinationsGrid = document.querySelector(".destinations-grid");
    if (toggleDestinationsBtn && destinationsGrid) {
        toggleDestinationsBtn.addEventListener("click", () => {
            toggleDestinationsBtn.classList.toggle("collapsed");
            destinationsGrid.classList.toggle("collapsed");
        });
    }

    const toggleHotelsBtn = document.getElementById("toggleHotelsBtn");
    const featuredHotelsGrid = document.getElementById("featuredHotelsGrid");
    if (toggleHotelsBtn && featuredHotelsGrid) {
        toggleHotelsBtn.addEventListener("click", () => {
            toggleHotelsBtn.classList.toggle("collapsed");
            featuredHotelsGrid.classList.toggle("collapsed");
        });
    }

    // 5. City Filter Pills on Homepage
    const cityPills = document.querySelectorAll("#cityFilterPills .pill-btn");
    cityPills.forEach(pill => {
        pill.addEventListener("click", () => {
            cityPills.forEach(p => p.classList.remove("active"));
            pill.classList.add("active");
            const targetCity = pill.dataset.city;

            const cards = document.querySelectorAll(".hotel-card");
            cards.forEach(card => {
                if (targetCity === "all" || card.dataset.city === targetCity) {
                    card.style.display = "";
                } else {
                    card.style.display = "none";
                }
            });
        });
    });

    // 6. Search Tab switcher
    const searchTabs = document.querySelectorAll("#searchTabsContainer .tab-btn");
    searchTabs.forEach(tab => {
        tab.addEventListener("click", () => {
            searchTabs.forEach(t => t.classList.remove("active"));
            tab.classList.add("active");
            const categoryInput = document.getElementById("searchCategoryInput");
            if (categoryInput) {
                categoryInput.value = tab.dataset.type === "all" ? "" : tab.dataset.type;
            }
        });
    });
});

// 7. Favorite Toggle Helper
function toggleFavorite(hotelId, contextPath) {
    fetch((contextPath || "") + "/favorite", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "hotelId=" + encodeURIComponent(hotelId)
    })
    .then(res => {
        if (res.status === 401) {
            alert("Vui lòng đăng nhập để lưu khách sạn yêu thích!");
            window.location.href = (contextPath || "") + "/login";
            return null;
        }
        return res.json();
    })
    .then(data => {
        if (!data) return;
        if (data.status === "added") {
            alert("Đã thêm vào danh sách yêu thích!");
            const btn = document.querySelector(`.btn-fav[data-id="${hotelId}"]`);
            if (btn) btn.classList.add("active");
        } else if (data.status === "removed") {
            alert("Đã xóa khỏi danh sách yêu thích!");
            const btn = document.querySelector(`.btn-fav[data-id="${hotelId}"]`);
            if (btn) btn.classList.remove("active");
        }
    })
    .catch(err => {
        console.error("Lỗi yêu thích:", err);
    });
}
