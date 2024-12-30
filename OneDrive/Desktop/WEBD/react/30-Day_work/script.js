document.addEventListener('DOMContentLoaded', () => {
    const postsContainer = document.querySelector('.posts-grid');
  
    // Fetch and display posts
    fetch('http://localhost:3000/posts')
      .then(response => response.json())
      .then(posts => {
        posts.forEach(post => {
          const article = document.createElement('article');
          article.innerHTML = `
            <img src="${post.image}" alt="${post.title}">
            <h3>${post.title}</h3>
            <p>${post.description}</p>
            <a href="#">Read More</a>
            <button onclick="deletePost(${post.id})">Delete</button>
          `;
          postsContainer.appendChild(article);
        });
      });
  });
  
  function deletePost(id) {
    fetch(`http://localhost:3000/posts/${id}`, {
      method: 'DELETE'
    })
    .then(response => response.json())
    .then(() => {
      location.reload();
    });
  }
  
  function createPost(event) {
    event.preventDefault();
    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const image = document.getElementById('image').value;
  
    fetch('http://localhost:3000/posts', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ title, description, image })
    })
    .then(response => response.json())
    .then(() => {
      location.reload();
    });
  }
  
  function updatePost(event) {
    event.preventDefault();
    const id = document.getElementById('update-id').value;
    const title = document.getElementById('update-title').value;
    const description = document.getElementById('update-description').value;
    const image = document.getElementById('update-image').value;
  
    fetch(`http://localhost:3000/posts/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ title, description, image })
    })
    .then(response => response.json())
    .then(() => {
      location.reload();
    });
  }
  