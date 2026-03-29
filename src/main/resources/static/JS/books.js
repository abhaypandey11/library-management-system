// ============================================
//   LibraryOS — Books JS
// ============================================

var allBooks = [];

// Load all books on page load
async function loadBooks() {
    try {
        var result = await apiGet('/books');
        if (result.success) {
            allBooks = result.data;
            renderBooksTable(allBooks);
        } else {
            showTableError('booksTableBody', 8, result.message);
        }
    } catch (err) {
        showTableError('booksTableBody', 8, '⚠️ Could not connect to server.');
    }
}

function renderBooksTable(books) {
    var tbody = document.getElementById('booksTableBody');
    var countEl = document.getElementById('bookCount');
    countEl.textContent = books.length + ' records';

    if (books.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="loading-row">No books found. Add your first book!</td></tr>';
        return;
    }

    var html = '';
    for (var i = 0; i < books.length; i++) {
        var book = books[i];

        var availableClass = 'good';
        if (book.availableCopies === 0) {
            availableClass = 'none';
        } else if (book.availableCopies <= 2) {
            availableClass = 'low';
        }

        html += '<tr>';
        html += '<td>' + book.id + '</td>';
        html += '<td><strong style="color:var(--text-primary)">' + book.title + '</strong></td>';
        html += '<td>' + book.author + '</td>';
        html += '<td><code style="font-size:11px;color:var(--text-muted)">' + book.isbn + '</code></td>';
        html += '<td>' + book.category + '</td>';
        html += '<td>' + book.totalCopies + '</td>';
        html += '<td><span class="available-chip ' + availableClass + '">' + book.availableCopies + '</span></td>';
        html += '<td>';
        html += '<div class="action-btns">';
        html += '<button class="btn btn-sm btn-outline" onclick="openEditBook(' + JSON.stringify(book).replace(/"/g, '&quot;') + ')">✏️ Edit</button>';
        html += '<button class="btn btn-sm btn-ghost" onclick="deleteBook(' + book.id + ')">🗑️</button>';
        html += '</div>';
        html += '</td>';
        html += '</tr>';
    }
    tbody.innerHTML = html;
}

// Filter books by search input
function filterBooks() {
    var query = document.getElementById('bookSearch').value.toLowerCase();
    var filtered = [];
    for (var i = 0; i < allBooks.length; i++) {
        var book = allBooks[i];
        if (
            book.title.toLowerCase().indexOf(query) !== -1 ||
            book.author.toLowerCase().indexOf(query) !== -1 ||
            book.category.toLowerCase().indexOf(query) !== -1
        ) {
            filtered.push(book);
        }
    }
    renderBooksTable(filtered);
}

// Add new book
async function addBook() {
    hideError('addBookError');

    var dto = {
        title: document.getElementById('addTitle').value.trim(),
        author: document.getElementById('addAuthor').value.trim(),
        isbn: document.getElementById('addIsbn').value.trim(),
        category: document.getElementById('addCategory').value.trim(),
        totalCopies: parseInt(document.getElementById('addTotalCopies').value)
    };

    try {
        var result = await apiPost('/books', dto);
        if (result.success) {
            closeModal('addBookModal');
            clearAddForm();
            showToast('✅ Book added successfully!', 'success');
            loadBooks();
        } else {
            showErrors('addBookError', result.data || result.message);
        }
    } catch (err) {
        showErrors('addBookError', 'Server error. Please try again.');
    }
}

// Open edit modal
function openEditBook(book) {
    document.getElementById('editBookId').value = book.id;
    document.getElementById('editTitle').value = book.title;
    document.getElementById('editAuthor').value = book.author;
    document.getElementById('editIsbn').value = book.isbn;
    document.getElementById('editCategory').value = book.category;
    document.getElementById('editTotalCopies').value = book.totalCopies;
    hideError('editBookError');
    openModal('editBookModal');
}

// Update book
async function updateBook() {
    hideError('editBookError');

    var id = document.getElementById('editBookId').value;
    var dto = {
        title: document.getElementById('editTitle').value.trim(),
        author: document.getElementById('editAuthor').value.trim(),
        isbn: document.getElementById('editIsbn').value.trim(),
        category: document.getElementById('editCategory').value.trim(),
        totalCopies: parseInt(document.getElementById('editTotalCopies').value)
    };

    try {
        var result = await apiPut('/books/' + id, dto);
        if (result.success) {
            closeModal('editBookModal');
            showToast('✅ Book updated successfully!', 'success');
            loadBooks();
        } else {
            showErrors('editBookError', result.data || result.message);
        }
    } catch (err) {
        showErrors('editBookError', 'Server error. Please try again.');
    }
}

// Delete book
async function deleteBook(id) {
    if (!confirm('Are you sure you want to delete this book?')) return;
    try {
        var result = await apiDelete('/books/' + id);
        if (result.success) {
            showToast('🗑️ Book deleted successfully!', 'success');
            loadBooks();
        } else {
            showToast('❌ ' + result.message, 'error');
        }
    } catch (err) {
        showToast('❌ Server error.', 'error');
    }
}

function clearAddForm() {
    document.getElementById('addTitle').value = '';
    document.getElementById('addAuthor').value = '';
    document.getElementById('addIsbn').value = '';
    document.getElementById('addCategory').value = '';
    document.getElementById('addTotalCopies').value = '';
}

function showTableError(tbodyId, cols, message) {
    document.getElementById(tbodyId).innerHTML =
        '<tr><td colspan="' + cols + '" class="loading-row">' + message + '</td></tr>';
}

// Load on page ready
loadBooks();
