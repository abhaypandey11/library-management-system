// ============================================
//   LibraryOS — Dashboard JS
// ============================================

// Show current date
var dateEl = document.getElementById('currentDate');
if (dateEl) {
    var now = new Date();
    dateEl.textContent = now.toLocaleDateString('en-IN', {
        weekday: 'long', day: 'numeric', month: 'long', year: 'numeric'
    });
}

// Load all dashboard data
async function loadDashboard() {
    try {
        // Fetch books, members, issues in parallel
        var booksResult = await apiGet('/books');
        var membersResult = await apiGet('/members');
        var issuesResult = await apiGet('/issues');

        var books = booksResult.success ? booksResult.data : [];
        var members = membersResult.success ? membersResult.data : [];
        var issues = issuesResult.success ? issuesResult.data : [];

        // Count stats
        var totalIssued = 0;
        var totalReturned = 0;
        for (var i = 0; i < issues.length; i++) {
            if (issues[i].status === 'ISSUED') {
                totalIssued++;
            } else if (issues[i].status === 'RETURNED') {
                totalReturned++;
            }
        }

        // Update stat cards
        document.getElementById('totalBooks').textContent = books.length;
        document.getElementById('totalMembers').textContent = members.length;
        document.getElementById('totalIssued').textContent = totalIssued;
        document.getElementById('totalReturned').textContent = totalReturned;

        // Load recent issues table (last 8)
        var recentIssues = issues.slice(-8).reverse();
        loadRecentIssuesTable(recentIssues);

    } catch (err) {
        console.error('Dashboard load error:', err);
        document.getElementById('recentIssuesBody').innerHTML =
            '<tr><td colspan="7" class="loading-row">⚠️ Could not connect to server. Make sure Spring Boot is running.</td></tr>';
    }
}

function loadRecentIssuesTable(issues) {
    var tbody = document.getElementById('recentIssuesBody');

    if (issues.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="loading-row">No issue records found.</td></tr>';
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

        html += '<tr>';
        html += '<td>' + issue.id + '</td>';
        html += '<td><strong>' + issue.bookTitle + '</strong></td>';
        html += '<td>' + issue.memberName + '</td>';
        html += '<td>' + formatDate(issue.issueDate) + '</td>';
        html += '<td>' + formatDate(issue.dueDate) + '</td>';
        html += '<td>' + statusBadge + '</td>';
        html += '<td>' + fineText + '</td>';
        html += '</tr>';
    }

    tbody.innerHTML = html;
}

// Load on page ready
loadDashboard();
