import React, {useState } from 'react';

export function BuchTable({ productList }){

    const [isChecked, setIsChecked] = useState(false);

    const filteredList = productList.filter((product) => {
        // Replace the condition with one that matches the specific type of product you want for Table A
        return product.isbn;
    });

    const handleButtonClick = () => {
        setIsChecked(!isChecked)
    }

    if(filteredList.length > 0){
        return(
            <h3> <div onClick={handleButtonClick}>Bücher</div>
            {isChecked &&
                <table className="produktTable">
                    <thead>
                        <tr>
                            <th>Produktnummer</th>
                            <th>Titel</th>
                            <th>Rating</th>
                            <th>Rang</th>
                            <th>Bild</th>
                            <th>Seitenzahl</th>
                            <th>Erscheinungsdatum</th>
                            <th>ISBN</th>
                            <th>Authoren</th>
                            <th>Verlage</th>
                            {/* Add specific attributes for Table A */}
                            </tr>
                    </thead>
                    <tbody>
                        {filteredList.map( (product, index)=> (
                        <tr key={index}>
                            <td>{product.prodnr}</td>
                            <td>{product.titel}</td>
                            <td>{product.rating}</td>
                            <td>{product.rang}</td>
                            <td>{product.bild}</td>
                            <td>{product.seitenzahl}</td>
                            <td>{product.erscheinungsjahr}</td>
                            <td>{product.isbn}</td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Autoren Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {product.autoren.map( (autor, autorIndex) => (
                                            <tr key={autorIndex}>
                                                <td>{autor}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Verlags Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {product.verlage.map( (verlag, verlagIndex) => (
                                                <tr key ={verlagIndex}>
                                                    <td>{verlag}</td>
                                                </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </td>
                            {/* Add specific attributes for Table A */}
                        </tr>
                        ))}
                    </tbody>
                </table>
                }   
            </h3>
        )
    }
}

export default BuchTable;