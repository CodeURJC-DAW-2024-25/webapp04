document.getElementById('profilePicInput').addEventListener('change', function (event) {
    const [file] = event.target.files;
    if (file) {
        const profilePicPreview = document.getElementById('pfp-preview');
        profilePicPreview.src = URL.createObjectURL(file);
    }
});