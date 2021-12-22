import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js'
import { getAuth } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js'
import { logout } from './accounts.js'
 
const firebaseConfig = {
    apiKey: "AIzaSyDxUQZc18COhEX0v5Uzbxm_26EeSUiormw",
    authDomain: "splat-ce92b.firebaseapp.com",
    projectId: "splat-ce92b",
    storageBucket: "splat-ce92b.appspot.com",
    messagingSenderId: "168823335000",
    appId: "1:168823335000:web:3eea4012a5264f4343822d"
  };
  
const app = initializeApp(firebaseConfig);

const auth = getAuth();

window.logout = logout;
auth.onIdTokenChanged(function(user) {
    let yourReviews = document.getElementById('yourReviewsA')
    let loginA = document.getElementById('loginA')
    let logoutA = document.getElementById('logoutA')

    if (user) {
        if (Date.now() - user.metadata.lastLoginAt > 3540000) {
            logout();
          }
        else {
            yourReviews.style.display = 'inline';
            loginA.style.display = 'none';
            logoutA.style.display = 'inline';
            document.getElementsByTagName('html')[0].style.visibility = "visible";
        }
    }
    else {
        window.location.href = 'Login.html';
    }
  });

window.addReview = function addReview() {
  let restName = document.getElementById("rname").value;
  let bodyText = document.getElementById("review").value;
  let title = document.getElementById("rtitle").value;
  let rating = -1;
  if (restName.length > 50) {
    alert("Restaurant name cannot exceed 50 characters")
  }
  else if (bodyText.length > 300) {
    alert("Review cannot exceed 300 characters")
  }
  else if (title.length > 50) {
    alert("Review title cannot exceed 50 characters")
  }
  else if (!checkRating()) {
    alert("Please select a rating")
  }
  else {
    rating = checkRating();
    let sessionToken = auth.currentUser.accessToken;

    let requestObject = {
      "sessionToken": sessionToken,
      "title": title,
      "bodyText": bodyText,
      "rating": rating,
      "restName": restName
  }

  fetch('https://splat.azurewebsites.net/reviews/addReview', {
  method: 'POST', 
  headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
  },
  body: JSON.stringify(requestObject),
  })
  .then((response) => {
      if (response.ok) {
          alert("Review accepted");
          window.location.href = 'index.html'
      } 
      else {
          response.json().then(data => {
              alert(data.error);
          })
          throw new Error('Something went wrong');
      }
  })
  .catch((error) => {
      console.log(error)
  });

  }
}

function checkRating() {
  if (document.getElementById("rating-1").checked) {
    return 1
  }
  else if (document.getElementById("rating-2").checked) {
    return 2
  }
  else if (document.getElementById("rating-3").checked) {
    return 3
  }
  else if (document.getElementById("rating-4").checked) {
    return 4
  }
  else if (document.getElementById("rating-5").checked) {
    return 5
  }
  else {
    return false;
  }
}

