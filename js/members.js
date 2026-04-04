// ============================================
//   LibraryOS — Members JS
// ============================================

var allMembers = [];

async function loadMembers() {
    try {
        var result = await apiGet('/members');
        if (result.success) {
            allMembers = result.data;
            renderMembersTable(allMembers);
        } else {
            showTableError('membersTableBody', 6, result.message);
        }
    } catch (err) {
        showTableError('membersTableBody', 6, '⚠️ Could not connect to server.');
    }
}

function renderMembersTable(members) {
    var tbody = document.getElementById('membersTableBody');
    var countEl = document.getElementById('memberCount');
    countEl.textContent = members.length + ' records';

    if (members.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading-row">No members found. Register your first member!</td></tr>';
        return;
    }

    var html = '';
    for (var i = 0; i < members.length; i++) {
        var member = members[i];

        var typeBadgeClass = 'badge-student';
        if (member.memberType === 'Teacher') {
            typeBadgeClass = 'badge-teacher';
        } else if (member.memberType === 'Staff') {
            typeBadgeClass = 'badge-staff';
        }

        html += '<tr>';
        html += '<td>' + member.id + '</td>';
        html += '<td><strong style="color:var(--text-primary)">' + member.name + '</strong></td>';
        html += '<td>' + member.email + '</td>';
        html += '<td>' + member.phone + '</td>';
        html += '<td><span class="badge ' + typeBadgeClass + '">' + member.memberType + '</span></td>';
        html += '<td>';
        html += '<div class="action-btns">';
        html += '<button class="btn btn-sm btn-outline" onclick="openEditMember(' + JSON.stringify(member).replace(/"/g, '&quot;') + ')">✏️ Edit</button>';
        html += '<button class="btn btn-sm btn-ghost" onclick="deleteMember(' + member.id + ')">🗑️</button>';
        html += '</div>';
        html += '</td>';
        html += '</tr>';
    }
    tbody.innerHTML = html;
}

function filterMembers() {
    var query = document.getElementById('memberSearch').value.toLowerCase();
    var filtered = [];
    for (var i = 0; i < allMembers.length; i++) {
        var member = allMembers[i];
        if (
            member.name.toLowerCase().indexOf(query) !== -1 ||
            member.email.toLowerCase().indexOf(query) !== -1 ||
            member.memberType.toLowerCase().indexOf(query) !== -1
        ) {
            filtered.push(member);
        }
    }
    renderMembersTable(filtered);
}

async function addMember() {
    hideError('addMemberError');

    var dto = {
        name: document.getElementById('addName').value.trim(),
        email: document.getElementById('addEmail').value.trim(),
        phone: document.getElementById('addPhone').value.trim(),
        memberType: document.getElementById('addMemberType').value
    };

    try {
        var result = await apiPost('/members', dto);
        if (result.success) {
            closeModal('addMemberModal');
            clearAddMemberForm();
            showToast('✅ Member registered successfully!', 'success');
            loadMembers();
        } else {
            showErrors('addMemberError', result.data || result.message);
        }
    } catch (err) {
        showErrors('addMemberError', 'Server error. Please try again.');
    }
}

function openEditMember(member) {
    document.getElementById('editMemberId').value = member.id;
    document.getElementById('editName').value = member.name;
    document.getElementById('editEmail').value = member.email;
    document.getElementById('editPhone').value = member.phone;
    document.getElementById('editMemberType').value = member.memberType;
    hideError('editMemberError');
    openModal('editMemberModal');
}

async function updateMember() {
    hideError('editMemberError');

    var id = document.getElementById('editMemberId').value;
    var dto = {
        name: document.getElementById('editName').value.trim(),
        email: document.getElementById('editEmail').value.trim(),
        phone: document.getElementById('editPhone').value.trim(),
        memberType: document.getElementById('editMemberType').value
    };

    try {
        var result = await apiPut('/members/' + id, dto);
        if (result.success) {
            closeModal('editMemberModal');
            showToast('✅ Member updated successfully!', 'success');
            loadMembers();
        } else {
            showErrors('editMemberError', result.data || result.message);
        }
    } catch (err) {
        showErrors('editMemberError', 'Server error. Please try again.');
    }
}

async function deleteMember(id) {
    if (!confirm('Are you sure you want to delete this member?')) return;
    try {
        var result = await apiDelete('/members/' + id);
        if (result.success) {
            showToast('🗑️ Member deleted successfully!', 'success');
            loadMembers();
        } else {
            showToast('❌ ' + result.message, 'error');
        }
    } catch (err) {
        showToast('❌ Server error.', 'error');
    }
}

function clearAddMemberForm() {
    document.getElementById('addName').value = '';
    document.getElementById('addEmail').value = '';
    document.getElementById('addPhone').value = '';
    document.getElementById('addMemberType').value = '';
}

function showTableError(tbodyId, cols, message) {
    document.getElementById(tbodyId).innerHTML =
        '<tr><td colspan="' + cols + '" class="loading-row">' + message + '</td></tr>';
}

loadMembers();
