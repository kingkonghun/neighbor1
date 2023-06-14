const toggleBtn = document.querySelector('.navbar__toogleBtn');
const menu = document.querySelector('.navbar__menu');
const icons = document.querySelector('.navbar__icons');
//클릭할때마다 지정하는 함수를 호출{} 엑티브 
toggleBtn.addEventListener('click', () => {
    menu.classList.toggle('active');
    icons.classList.toggle('active');
});


//마우스 가져갈때 변화
//var content = document.getElementById('content');
//var small = content.querySelector('.small');
//var large = content.querySelector('.large');

//content.addEventListener('mouseover', function() {
//  small.style.display = 'none';
 // large.style.display = 'block';
//});

//content.addEventListener('mouseout', function() {
//  small.style.display = 'block';
//  large.style.display = 'none';
//});

//스크롤 시 추가 생산되는 박스  유튜브유노코딩 바닐라 자바스크립트무한 스크롤 구현
var count = 4;
window.onscroll = function() {
  if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight) {
    var toAdd = document.createElement("div");
    toAdd.classList.add("accordion");
    toAdd.innerHTML = `
      <input type="checkbox" id="answer${count}">
      <label for="answer${count}"><img src="이미지_경로" alt="프로필 사진">
      모임 제목부분<em></em></label>
      <div><p>자세한 모임정보</p></div>
    `;
    document.querySelector('.accordion-container').appendChild(toAdd);
    count++;
  }
};
