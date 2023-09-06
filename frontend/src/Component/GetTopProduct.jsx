import React, {useState } from 'react';
import axios from 'axios';
import TopProductSearch from './TopProductSearch'
import BuchTable from './Tables/BuchTable'
import CDTable from './Tables/CDTable'
import DVDTable from './Tables/DVDTable'

export function GetTopProduct(){

    const [productList, setProductList] = useState([]);
    const [hasSearched, setHasSearched] = useState(false);

    const fetchProductsPattern = (limit) => {
        setHasSearched(true);
        axios
          .get(`http://localhost:8080/getTopProducts?limit=${limit}`)
          .then((res) => setProductList(res.data))
          .catch((err) => console.log(err));
      };

    const handleSearch = (limit) => {
        fetchProductsPattern(limit)
    }


    return(
        <div>
        <TopProductSearch onSearch={handleSearch}/>
        {hasSearched && productList && productList.length > 0 && (
            <div>
                <BuchTable productList={productList}/>
                <CDTable productList={productList}/>
                <DVDTable productList={productList}/>
            </div>
        )}
        {hasSearched && productList.length <1 && (
            <h4>Kein Produkt gefunden</h4>
        )}
        </div>
    )
}

export default GetTopProduct;