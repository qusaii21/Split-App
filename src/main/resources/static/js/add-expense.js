const form = document.getElementById('expense-form');
const msg = document.getElementById('msg');

form.addEventListener('submit', async e => {
  e.preventDefault();
  const payload = {
    description: form.description.value,
    amount: parseFloat(form.amount.value),
    paidBy: form.paidBy.value,
    date: form.date.value,
    category: form.category.value,
    shareType: form.shareType.value
  };
  const resp = await fetch('/api/expenses', {
    method: 'POST',
    headers: {'Content-Type':'application/json'},
    body: JSON.stringify(payload)
  });
  const json = await resp.json();
  if (resp.ok) {
    msg.textContent = 'Expense created!';
    form.reset();
  } else {
    msg.textContent = 'Error: ' + (json.message||resp.statusText);
  }
});
