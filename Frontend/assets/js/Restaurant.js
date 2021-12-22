import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-app.js'
import { getAuth } from 'https://www.gstatic.com/firebasejs/9.5.0/firebase-auth.js'
import { logout } from './accounts.js'
 

let restName = localStorage.getItem("viewRestaurant");

if (restName == null) {
  window.location.href = 'index.html'
}

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
        // 59 minutes since user logged in
        if (Date.now() - user.metadata.lastLoginAt > 3540000) {
          logout();
        }
        else {
          yourReviews.style.display = 'inline';
          loginA.style.display = 'none';
          logoutA.style.display = 'inline';
        }
    }
    else {
        yourReviews.style.display = 'none';
        loginA.style.display = 'inline';
        logoutA.style.display = 'none';
    }
    renderBody();
  });


  function renderBody() {
    let showRestName = toTitleCase(restName);
    document.getElementsByTagName('title')[0].innerHTML = toTitleCase(showRestName);
    document.getElementById('restTitle').innerHTML = showRestName;

    fetch('https://splat.azurewebsites.net/reviews/restaurant?restName=' + restName, {
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
                document.getElementById("restScore").innerHTML = 'Average score: ' + data.average + '/5';

                let content = document.getElementById("mainContent");

                data.reviews.forEach(element => 
                  {
                    content.appendChild(generateReview(element));
                  });
              });
              document.getElementsByTagName('html')[0].style.visibility = "visible";
          }
          else {
              alert(restName + " has no reviews yet. Be the first!")
              window.location.href = 'index.html'
          }
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

function generateReview(rawReview) {
  let newReviewTop = document.createElement('div');
  newReviewTop.setAttribute('class', 'review');

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
  reviewDate.setAttribute('class', 'reviewDate');
  let actualDate = document.createElement('p');
  let convertDate = new Date(rawReview.created);
  actualDate.innerHTML = 'Posted on ' + convertDate.toLocaleDateString();
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