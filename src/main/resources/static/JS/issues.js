// ============================================
//   LibraryOS — Issues JS
// ============================================

var allIssues = [];
var currentReturnId = null;

async function loadIssues() {
    try {
        var result = await apiGet('/issues');
        if (result.success) {
            allIssues = result.data;
            renderIssuesTable(allIssues);
        } else {
            showTableError('issuesTableBody', 9, result.message);
        }
    } catch (err) {
        showTableError('issuesTableBody', 9, '⚠️ Could not connect to server.');
    }
}

function renderIssuesTable(issues) {
    var tbody = document.getElementById('issuesTableBody');
    var countEl = document.getElementById('issueCount');
    countEl.textContent = issues.length + ' records';

    if (issues.length === 0) {
        tbody.innerHTML = '<tr><td colspan="9" class="loading-row">No issue records found.</td></tr>';
        return;
    }

    var html = '';
    for (var i = 0; i < issues.length; i++) {
        var issue = issues[i];

        var statusBadge = issue.status === 'ISSUED'
            ? '<span class="badge badge-issued">ISSUED</span>'
            : '<span class="badge badge-returned">RETURNED</span>';

        var fineText = issue.fine > 0
            ? '<span class="fine-cell has-fine">₹' + issue.fine + '</span>'
            : '<span class="fine-cell no-fine">₹0</span>';

        var actionBtn = '';
        if (issue.status === 'ISSUED') {
            actionBtn = '<button class="btn btn-sm btn-outline" onclick="openReturnModal(' + issue.id + ', \'' + issue.bookTitle + '\', \'' + issue.memberName + '\', \'' + issue.dueDate + '\')">↩️ Return</button>';
        } else {
            actionBtn = '<span style="color:var(--text-muted);font-size:12px">Completed</span>';
        }

        html += '<tr>';
        html += '<td>' + issue.id + '</td>';
        html += '<td><strong style="color:var(--text-primary)">' + issue.bookTitle + '</strong></td>';
        html += '<td>' + issue.memberName + '</td>';
        html += '<td>' + formatDate(issue.issueDate) + '</td>';
        html += '<td>' + formatDate(issue.dueDate) + '</td>';
        html += '<td>' + formatDate(issue.returnDate) + '</td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>' + fineText + '</td>';
        html += '<td>' + actionBtn + '</td>';
        html += '</tr>';
    }
    tbody.innerHTML = html;
}

// Filter by status tab
function filterByStatus(status, btn) {
    // Update active tab
    var tabs = document.querySelectorAll('.tab-btn');
    for (var i = 0; i < tabs.length; i++) {
        tabs[i].classList.remove('active');
    }
    btn.classList.add('active');

    if (status === 'ALL') {
        renderIssuesTable(allIssues);
    } else {
        var filtered = [];
        for (var i = 0; i < allIssues.length; i++) {
            if (allIssues[i].status === status) {
                filtered.push(allIssues[i]);
            }
        }
        renderIssuesTable(filtered);
    }
}

// Load books and members into dropdowns
async function loadDropdowns() {
    try {
        var booksResult = await apiGet('/books');
        var membersResult = await apiGet('/members');

        var bookSelect = document.getElementById('issueBookId');
        var memberSelect = document.getElementById('issueMemberId');

        bookSelect.innerHTML = '<option value="">— Select a book —</option>';
        memberSelect.innerHTML = '<option value="">— Select a member —</option>';

        if (booksResult.success) {
            for (var i = 0; i < booksResult.data.length; i++) {
                var book = booksResult.data[i];
                if (book.availableCopies > 0) {
                    var option = document.createElement('option');
                    option.value = book.id;
                    option.textContent = book.title + ' (' + book.availableCopies + ' available)';
                    bookSelect.appendChild(option);
                }
            }
        }

        if (membersResult.success) {
            for (var i = 0; i < membersResult.data.length; i++) {
                var member = membersResult.data[i];
                var option = document.createElement('option');
                option.value = member.id;
                option.textContent = member.name + ' — ' + member.memberType;
                memberSelect.appendChild(option);
            }
        }
    } catch (err) {
        console.error('Dropdown load error:', err);
    }
}

// Issue book
async function issueBook() {
    hideError('issueBookError');

    var bookId = document.getElementById('issueBookId').value;
    var memberId = document.getElementById('issueMemberId').value;

    if (!bookId || !memberId) {
        showErrors('issueBookError', 'Please select both a book and a member.');
        return;
    }

    var dto = {
        bookId: parseInt(bookId),
        memberId: parseInt(memberId)
    };

    try {
        var result = await apiPost('/issues', dto);
        if (result.success) {
            closeModal('issueBookModal');
            showToast('✅ Book issued successfully!', 'success');
            loadIssues();
            loadDropdowns();
        } else {
            showErrors('issueBookError', result.data || result.message);
        }
    } catch (err) {
        showErrors('issueBookError', 'Server error. Please try again.');
    }
}

// Open return modal with fine preview
function openReturnModal(issueId, bookTitle, memberName, dueDate) {
    currentReturnId = issueId;

    var today = new Date();
    var due = new Date(dueDate);
    var daysLate = Math.floor((today - due) / (1000 * 60 * 60 * 24));
    var estimatedFine = daysLate > 0 ? daysLate * 5 : 0;

    var fineHtml = '';
    fineHtml += '<div style="margin-bottom:8px"><strong style="color:var(--text-primary)">' + bookTitle + '</strong></div>';
    fineHtml += '<div>Member: ' + memberName + '</div>';
    fineHtml += '<div>Due Date: ' + formatDate(dueDate) + '</div>';
    fineHtml += '<div>Return Date: ' + formatDate(today.toISOString().split('T')[0]) + '</div>';

    if (daysLate > 0) {
        fineHtml += '<div>Days Late: <strong style="color:var(--accent-red)">' + daysLate + ' days</strong></div>';
        fineHtml += '<div>Estimated Fine: <span class="fine-amount">₹' + estimatedFine + '</span></div>';
    } else {
        fineHtml += '<div style="margin-top:8px">Returned on time! 🎉</div>';
        fineHtml += '<span class="fine-amount no-fine">₹0 Fine</span>';
    }

    document.getElementById('fineDetailCard').innerHTML = fineHtml;
    openModal('fineModal');
}

// Confirm return
async function confirmReturn() {
    if (!currentReturnId) return;
    try {
        var result = await apiPut('/issues/return/' + currentReturnId);
        if (result.success) {
            closeModal('fineModal');
            var fine = result.data.fine;
            if (fine > 0) {
                showToast('↩️ Book returned. Fine: ₹' + fine, 'success');
            } else {
                showToast('↩️ Book returned successfully! No fine.', 'success');
            }
            currentReturnId = null;
            loadIssues();
            loadDropdowns();
        } else {
            showToast('❌ ' + result.message, 'error');
        }
    } catch (err) {
        showToast('❌ Server error.', 'error');
    }
}

function showTableError(tbodyId, cols, message) {
    document.getElementById(tbodyId).innerHTML =
        '<tr><td colspan="' + cols + '" class="loading-row">' + message + '</td></tr>';
}

// Load on page ready
loadIssues();
loadDropdowns();
