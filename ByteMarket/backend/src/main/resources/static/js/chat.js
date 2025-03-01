document.addEventListener("DOMContentLoaded", function () {
    const messagesContainer = document.getElementById('messages-container');
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
});

const messagesContainer = document.getElementById('messages-container');
const observer = new MutationObserver(() => {
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
});

observer.observe(messagesContainer, { childList: true });