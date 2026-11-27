const BASE_URL = 'https://jsonplaceholder.typicode.com';

async function fetchJson(path) {
  const response = await fetch(BASE_URL + path);

  // fetch only rejects on network failure, so HTTP errors are checked here
  if (!response.ok) {
    throw new Error('Request failed with status ' + response.status);
  }

  return response.json();
}

export function getUsers() {
  return fetchJson('/users');
}

export function getUser(id) {
  return fetchJson('/users/' + id);
}
