import logo from './logo.svg';
import React, { useEffect, useState } from 'react';
import './App.css';
import GetCategoryTree from './Component/GetCategoryTree';
import GetProduct from './Component/GetProduct';
import GetTopProduct from './Component/GetTopProduct';
import GetSimilarCheaperProducts from './Component/GetSimilarCheaperProducts';
import GetTrolls from './Component/GetTrolls'
import GetAngebote from './Component/GetAngebote';
import GetReviews from './Component/GetReviews';
import CreateReview from './Component/CreateReview';

function App() {


const [isCategoryTreeVisible, setIsCategoryTreeVisible] = useState(false);

const loadCategoryTree = () => {
  setIsCategoryTreeVisible(!isCategoryTreeVisible);
};

const [isProductVisible, setIsProductVisible] = useState(false);

const loadProduct = () => {
  setIsProductVisible(!isProductVisible);
};  

const [isTopProductVisible, setIsTopProductVisible] = useState(false);

const loadTopProduct = () => {
  setIsTopProductVisible(!isTopProductVisible);
};  

const [isSimCheapProdVisible, setIsSimCheapProdVisible] = useState(false);

const loadSimCheapProd = () => {
  setIsSimCheapProdVisible(!isSimCheapProdVisible);
};  

const [isTrollsVisible, setIsTrollsVisible] = useState(false);

const loadTrolls = () => {
  setIsTrollsVisible(!isTrollsVisible);
};  

const [isAngeboteVisible, setIsAngeboteVisible] = useState(false);

const loadAngebote = () => {
  setIsAngeboteVisible(!isAngeboteVisible);
};  

const [isReviewsVisible, setIsReviewsVisible] = useState(false);

const loadReviews = () => {
  setIsReviewsVisible(!isReviewsVisible);
};  

const [isCreateReviewVisible, setIsCreateReviewVisible] = useState(false);

const loadCreateReview = () => {
  setIsCreateReviewVisible(!isCreateReviewVisible);
};  

  return (
    <div className="App">
      <header className="App-header">
      

      <button onClick={loadCategoryTree}>Show/Hide Category Tree</button>
      {isCategoryTreeVisible && <GetCategoryTree />}

      <button onClick={loadProduct}>Show/Hide Product Select</button>
      {isProductVisible && <GetProduct/>}

      <button onClick={loadTopProduct}>Show/Hide Top Product Select</button>
      {isTopProductVisible && <GetTopProduct/>}
      
      <button onClick={loadSimCheapProd}>Show/Hide Similar Cheaper Products Select</button>
      {isSimCheapProdVisible && <GetSimilarCheaperProducts/>}

      <button onClick={loadTrolls}>Show/Hide Troll Select</button>
      {isTrollsVisible && <GetTrolls/>}
      
      <button onClick={loadAngebote}>Show/Hide Angebote Select</button>
      {isAngeboteVisible && <GetAngebote/>}

      <button onClick={loadReviews}>Show/Hide Review Select</button>
      {isReviewsVisible && <GetReviews/>}

      <button onClick={loadCreateReview}>Show/Hide Review Creation</button>
      {isCreateReviewVisible && <CreateReview/>}
        
      </header>
    </div>
  );
}

export default App;
