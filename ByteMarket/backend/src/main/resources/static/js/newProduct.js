document.addEventListener("DOMContentLoaded", function () {
    const categorySelect = document.getElementById("category");
    const selectedCategory = categorySelect.getAttribute("data-category");

    if (selectedCategory) {
        for (let option of categorySelect.options) {
            if (option.value === selectedCategory) {
                option.selected = true;
                break;
            }
        }
    }
});