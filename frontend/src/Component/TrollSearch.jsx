import React, { useState } from 'react';

function TrollSearch({ onSearch }) {
  const [limit, setLimit] = useState('');

  const handleProdnrChange = (e) => {
    setLimit
(e.target.value);
  };

  const handleButtonClick = () => {
    // Pass the prodnr and isChecked values to the parent component's callback function
    onSearch(limit);
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Enter Review Cutoff Point"
        value={limit}
        onChange={handleProdnrChange}
      />
      <br/>
      <button onClick={handleButtonClick}>Fetch Trolls</button>
    </div>
  );
}

export default TrollSearch;
