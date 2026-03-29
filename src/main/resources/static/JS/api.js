// ============================================
//   LibraryOS — API Base Configuration
// ============================================

const BASE_URL = 'http://localhost:8080/api';

// ✅ Generic GET request
async function apiGet(endpoint) {
    const response = await fetch(BASE_URL + endpoint);
    const result = await response.json();
    return result;
}

// ✅ Generic POST request
async function apiPost(endpoint, body) {
    const response = await fetch(BASE_URL + endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    });
    const result = await response.json();
    return result;
}

// ✅ Generic PUT request
async function apiPut(endpoint, body) {
    const response = await fetch(BASE_URL + endpoint, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: body !== undefined ? JSON.stringify(body) : undefined
    });
    const result = await response.json();
    return result;
}

// ✅ Generic DELETE request
async function apiDelete(endpoint) {
    const response = await fetch(BASE_URL + endpoint, {
        method: 'DELETE'
    });
    const result = await response.json();
    return result;
}

// ✅ Open modal
function openModal(id) {
    document.getElementById(id).classList.add('open');
}

// ✅ Close modal
function closeModal(id) {
    document.getElementById(id).classList.remove('open');
}

// ✅ Show toast notification
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = 'toast ' + type + ' show';
    setTimeout(function() {
        toast.className = 'toast';
    }, 3500);
}

// ✅ Format date nicely
function formatDate(dateStr) {
    if (!dateStr) return '—';
    var date = new Date(dateStr);
    return date.toLocaleDateString('en-IN', {
        day: '2-digit',
        month: 'short',
        year: 'numeric'
    });
}

// ✅ Show validation errors from backend
function showErrors(errorBoxId, data) {
    var box = document.getElementById(errorBoxId);
    if (typeof data === 'object' && data !== null) {
        var messages = '';
        var keys = Object.keys(data);
        for (var i = 0; i < keys.length; i++) {
            messages += '• ' + data[keys[i]] + '\n';
        }
        box.style.whiteSpace = 'pre-line';
        box.textContent = messages;
    } else {
        box.textContent = data;
    }
    box.style.display = 'block';
}

// ✅ Hide error box
function hideError(errorBoxId) {
    var box = document.getElementById(errorBoxId);
    if (box) {
        box.style.display = 'none';
        box.textContent = '';
    }
}
