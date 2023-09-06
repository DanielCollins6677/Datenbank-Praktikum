import React, { useEffect, useState } from 'react';
import axios from 'axios';
import BuchTable from './Tables/BuchTable'
import CDTable from './Tables/CDTable'
import DVDTable from './Tables/DVDTable'

function CategoryTree({ category, path }) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [showCategoryProducts, setShowCategoryProducts] = useState(false);
  const [productList, setProductList] = useState([]);
  const pathStr = String(path).replace(/ /g,'+');

  if (!category) {
    return null;
  }

  const toggleExpansion = () => {
    setIsExpanded(!isExpanded);
  };

  const fetchProdukte = () => {
    const updatedPathStr = `${pathStr}/${category.name}`;
    // Only fetch products when the "fetch Produkte" span is clicked
    axios
      .get(`http://localhost:8080/getProductsByCategoryPath?path=${updatedPathStr}`)
      .then((res) => {
        setProductList(res.data);
        setShowCategoryProducts(true); // Show the products after fetching
      })
      .catch((err) => console.log(err));
  };

  return (
    <ul>
      <li>
        <div onClick={toggleExpansion}>
          {category.name}
          <span style={{ cursor: 'pointer' }}>
            {isExpanded ? ' [-]' : ' [+]'}
          </span>
        </div>
        {!isExpanded && !showCategoryProducts && (
          <span onClick={fetchProdukte} style={{ cursor: 'pointer' }}>
            [fetch Produkte]
          </span>
        )}
      </li>

      {showCategoryProducts && (<div>
          <BuchTable productList={productList}/>
          <CDTable productList={productList}/>
          <DVDTable productList={productList}/>
        </div>
      )}

      {isExpanded &&
        category.unterkategorien &&
        category.unterkategorien.length > 0 && (
          <ul>
            {category.unterkategorien.map((subcategory, index) => (
              <CategoryTree
                key={index}
                category={subcategory}
                path={`${path}/${category.name}`}
              />
            ))}
          </ul>
        )}
    </ul>
  );
}

function GetCategoryTree() {
  const [tree, setTree] = useState({});

  useEffect(() => {
    axios
      .get('http://localhost:8080/getCategoryTree')
      .then((res) => setTree(res.data))
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      Fetch CategoryTree
      <CategoryTree category={tree} path={''} />
    </div>
  );
}

export default GetCategoryTree;
