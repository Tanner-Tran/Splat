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
            renderBody()
        }
    }
    else {
        window.location.href = 'Login.html';
    }
  })

function renderBody() {
   let sessionToken = auth.currentUser.accessToken;

   fetch('https://splat-ce92b.ue.r.appspot.com/reviews/personal?sessionToken=' + sessionToken, {
    method: 'GET', 
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
    },
    })
    .then((response) => {
        if (response.ok) {
            response.json()
            .then(data => {

              let content = document.getElementById("mainContent");

              data.reviews.forEach(element => 
                {
                  content.appendChild(generateReview(element));
                });
            });
        }
        document.getElementsByTagName('html')[0].style.visibility = "visible";
    })
    .catch((error) => {
        console.log(error)
    });
}

function toTitleCase(str) {
  return str.replace(/\w\S*/g, function(txt){
      return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
  });
}

window.editRedirect = function editRedirect(restName, reviewTitle, bodyText, rating) {
    localStorage.setItem("editRestName", restName);
    localStorage.setItem("editReviewTitle", reviewTitle);
    localStorage.setItem("editBodyText", bodyText);
    localStorage.setItem("editRating", rating);

    window.location.href = "EditReview.html"
}

window.deleteReview = function deleteReview(restName) {
  let sessionToken = auth.currentUser.accessToken;

    let requestObject = {
      "sessionToken": sessionToken,
      "restName": restName
  }

  fetch('https://splat-ce92b.ue.r.appspot.com/reviews/deleteReview', {
  method: 'POST', 
  headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
  },
  body: JSON.stringify(requestObject),
  })
  .then((response) => {
      if (response.ok) {
          alert("Review deleted");
          window.location.reload();
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

  return false;
}
function generateReview(rawReview) {
let newReviewTop = document.createElement('div');
newReviewTop.setAttribute('class', 'editableReview');

let reviewButtons = document.createElement('div');
reviewButtons.setAttribute('class', 'reviewButtons');

let editButton = document.createElement('button');
editButton.setAttribute('type', 'button');
editButton.innerHTML = "Edit";
editButton.addEventListener('click', function(){
  let result = window.confirm('Are you sure?');

  if (result) {
    editRedirect(rawReview.restName, rawReview.title, rawReview.bodyText, rawReview.rating)
  }
});
reviewButtons.appendChild(editButton);

let deleteButton = document.createElement('button');
deleteButton.setAttribute('type', 'button');
deleteButton.innerHTML = "Delete";
deleteButton.addEventListener('click', function(){
  let result = window.confirm('Are you sure?');

  if (result) {
    deleteReview(rawReview.restName)
  }
});
reviewButtons.appendChild(deleteButton);


newReviewTop.appendChild(reviewButtons);

let reviewHeader = document.createElement('div');
reviewHeader.setAttribute('class', 'reviewHeader');

newReviewTop.appendChild(reviewHeader);


let reviewTitle = document.createElement('div');
reviewTitle.setAttribute('class', 'reviewTitle');
let actualTitle = document.createElement('h2');
actualTitle.innerHTML = rawReview.title;
reviewTitle.appendChild(actualTitle);

reviewHeader.appendChild(reviewTitle);

let reviewDate = document.createElement('div');
reviewDate.setAttribute('class', 'reviewCompanyAndDate');
let actualDate = document.createElement('p');
let convertDate = new Date(rawReview.created);
actualDate.innerHTML = 'About ' + toTitleCase(rawReview.restName) + ' on ' + convertDate.toLocaleDateString();
reviewDate.appendChild(actualDate);

reviewHeader.appendChild(reviewDate);

let reviewScore = document.createElement('div');
reviewScore.setAttribute('class', 'reviewScore');
let actualScore = document.createElement('h3');
actualScore.innerHTML = 'Score: ' + rawReview.rating + '/5';
reviewScore.appendChild(actualScore);

newReviewTop.appendChild(reviewScore);

let reviewBody = document.createElement('div');
reviewBody.setAttribute('class', 'reviewBody');
let actualBody = document.createElement('p');
actualBody.innerHTML = rawReview.bodyText;
reviewBody.appendChild(actualBody);

newReviewTop.appendChild(reviewBody);

return newReviewTop;
}

