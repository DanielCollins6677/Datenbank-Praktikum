import React, { useState } from 'react';

function ProductSearch({ onSearch }) {
  const [prodnr, setProdnr] = useState('');
  const [isChecked, setIsChecked] = useState(false);
  const prodnrStr = encodeURIComponent(prodnr);

  const handleProdnrChange = (e) => {
    setProdnr(e.target.value);
  };

  const handleCheckboxChange = () => {
    setIsChecked(!isChecked);
  };

  const handleButtonClick = () => {
    // Pass the prodnr and isChecked values to the parent component's callback function
    onSearch(prodnrStr, isChecked);
  };

  return (
    <div>
      <input
        type="text"
        placeholder="Enter Product Number"
        value={prodnr}
        onChange={handleProdnrChange}
      />
      <br/>
      <label>
        <input
          type="checkbox"
          checked={isChecked}
          onChange={handleCheckboxChange}
        />
        Patternsuche
      </label>
      <br />
      <button onClick={handleButtonClick}>Fetch Product</button>
    </div>
  );
}

export default ProductSearch;
