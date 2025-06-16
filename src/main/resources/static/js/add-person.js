const pForm = document.getElementById('person-form');
const pMsg  = document.getElementById('msg');

pForm.addEventListener('submit', e => {
  e.preventDefault();
  // locally store or send to server if you add an /api/people endpoint
  pMsg.textContent = `Person "${pForm.name.value}" added!`;
  pForm.reset();
});
