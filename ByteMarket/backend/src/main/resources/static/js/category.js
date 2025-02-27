document.addEventListener("DOMContentLoaded", function () {
    console.log("category.js loaded");

    const urlParams = new URLSearchParams(window.location.search);
    const categoryName = urlParams.get("category");

    console.log("Category from URL:", categoryName); // Para depuración

    if (categoryName) {
        document.getElementById("category-title").textContent = decodeURIComponent(categoryName);
    }
});