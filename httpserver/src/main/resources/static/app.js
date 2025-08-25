function loadPostMsg(name, age) {
    let url = "/api/users?name=" + name.value + "&age=" + age.value;

    fetch(url, { method: 'POST' })
        .then(response => {
            if (!response.ok) {
                throw new Error("Creation failed (status " + response.status + ")");
            }
            return response.json();
        })
        .then(user => renderUserCreated(user))
        .catch(err => alert("Error: " + err.message));
}
function loadDeleteMsg(id) {
    let url = "/api/users?id=" + id.value;

    fetch(url, { method: 'DELETE' })
        .then(response => {
            if (!response.ok) {
                throw new Error("Delete failed (status " + response.status + ")");
            }
            renderUserDeleted(id.value)
        })
        .catch(err => alert("Error: " + err.message));
}

function renderUserCreated(user) {
  const el = document.getElementById("postrespmsg");
  el.innerHTML = `
    <div class="card shadow-sm mt-3">
      <div class="card-body">
        <h5 class="card-title text-success">User Created Successfully!</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item"><strong>User Id: </strong> ${user.id}</li>
        </ul>
      </div>
    </div>
  `;
}
function renderUserDeleted(id) {
  const el = document.getElementById("deleterespmsg");
  el.innerHTML = `
    <div class="card shadow-sm mt-3">
      <div class="card-body">
        <h5 class="card-title text-success">User Deleted Successfully!</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item"><strong>User Id: </strong> ${id}</li>
        </ul>
      </div>
    </div>
  `;
}
function loadGetMsg(userId) {
    let url = "/api/users?id=" + userId.value;
    fetch(url, { method: 'GET' })    
    .then(r => {
      if (!r.ok) throw new Error(`Request failed (${r.status})`);
      return r.json();
    })
    .then(user => renderUserCard(user))
    .catch(err => alert("Error: " + err.message));
}
function renderUserCard(user) {
  const el = document.getElementById("getrespmsg");
  el.innerHTML = `
    <div class="card shadow-sm">
      <div class="card-body">
        <h5 class="card-title mb-3">User Id: ${user.id}</h5>
        <ul class="list-group list-group-flush">
          <li class="list-group-item"><strong>Name:</strong> <span id="u-name"></span></li>
          <li class="list-group-item"><strong>Age:</strong> <span id="u-age"></span></li>
        </ul>
      </div>
    </div>`;
  document.getElementById("u-name").textContent = user.name;
  document.getElementById("u-age").textContent  = user.age;
}



