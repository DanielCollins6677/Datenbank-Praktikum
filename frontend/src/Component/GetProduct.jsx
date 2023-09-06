import React, {useState } from 'react';
import axios from 'axios';
import './ProductTable.css';
import ProductSearch from './ProductSearch';
import BuchTable from './Tables/BuchTable'
import CDTable from './Tables/CDTable'
import DVDTable from './Tables/DVDTable'

function GetProduct() {
  const [product, setProduct] = useState({});
  const [hasSearched, setHasSearched] = useState(false); // State for the checkbox
  const [productList, setProductList] = useState([]);


  const handleSearch = (prodnrStr, isChecked) => {
    if(!isChecked) fetchProduct(prodnrStr);
    else fetchProductsPattern(prodnrStr);
    setHasSearched(true);
    // Perform the search logic based on prodnr and isChecked here
    // For example, you can make an API request with prodnr and the pattern search flag (isChecked)
    console.log('Searching with prodnr:', prodnrStr);
    console.log('Pattern search enabled:', isChecked);
  };

  const fetchProduct = (prodnr) => {
    axios
      .get(`http://localhost:8080/getProduct?prodnr=${prodnr}`)
      .then((res) => setProduct(res.data))
      .catch((err) => console.log(err));
      setProductList(null);
  };

  const fetchProductsPattern = (prodnr) => {
    axios
      .get(`http://localhost:8080/getProducts?pattern=${prodnr}`)
      .then((res) => setProductList(res.data))
      .catch((err) => console.log(err));
      setProduct(null);
  };

  //Wenn nichts oder ein Falsches Product eingegeben wurde
  if (hasSearched === true && (!product || product.length < 1) && (!productList || productList.length < 1)) {
        return (
            <div>
                <ProductSearch onSearch={handleSearch}>Fetch Produkte</ProductSearch>
                <p>Kein Produkt gefunden</p>
            </div>
        );
    }

    //Wenn die Patternsuche nicht ausgewählt ist und ein Produkt gefunden wurde
    if(hasSearched === true && !(!product || product.length < 1) && (!productList || productList.length < 1)){
        return (
            <div>
               <ProductSearch onSearch={handleSearch}>Fetch Produkte</ProductSearch>
                <table className="produktTable">
                    <thead>
                    <tr>
                        <th>Produktnummer</th>
                        <th>Titel</th>
                        <th>Rating</th>
                        <th>Rang</th>
                        <th>Bild</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{product.prodnr}</td>
                        <td>{product.titel}</td>
                        <td>{product.rating}</td>
                        <td>{product.rang}</td>
                        <td>{product.bild}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        );
    } else {
        return(
            <div>
                <ProductSearch onSearch={handleSearch}>Fetch Product</ProductSearch>
                <BuchTable productList={productList}/>
                <CDTable productList={productList}/>
                <DVDTable productList={productList}/>
            </div>
        );
    }
}

export default GetProduct;
