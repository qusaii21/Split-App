const summaryTable = document.querySelector('#summary-table tbody');
const balancesTable = document.querySelector('#balances-table tbody');
const settlementsTable = document.querySelector('#settlements-table tbody');
const yearInput = document.getElementById('year');
const monthInput = document.getElementById('month');
const refreshBtn = document.getElementById('refresh');

async function loadDashboard() {
  const year = yearInput.value;
  const month = monthInput.value;

  // 1. Summary
  let resp = await fetch(`/api/analytics/monthly-summary?year=${year}&month=${month}`);
  let json = await resp.json();
  summaryTable.innerHTML = '';
  for (const [cat, total] of Object.entries(json.data)) {
    const row = `<tr><td>${cat}</td><td>${total.toFixed(2)}</td></tr>`;
    summaryTable.insertAdjacentHTML('beforeend', row);
  }

  // 2. Balances
  resp = await fetch(`/api/balances`);
  json = await resp.json();
  balancesTable.innerHTML = '';
  json.data.forEach(b => {
    const row = `<tr><td>${b.name}</td><td>${b.balance.toFixed(2)}</td></tr>`;
    balancesTable.insertAdjacentHTML('beforeend', row);
  });

  // 3. Settlements
  resp = await fetch(`/api/settlements`);
  json = await resp.json();
  settlementsTable.innerHTML = '';
  json.data.forEach(s => {
    const row = `<tr><td>${s.from}</td><td>${s.to}</td><td>${s.amount.toFixed(2)}</td></tr>`;
    settlementsTable.insertAdjacentHTML('beforeend', row);
  });
}

refreshBtn.addEventListener('click', loadDashboard);
window.addEventListener('load', loadDashboard);
