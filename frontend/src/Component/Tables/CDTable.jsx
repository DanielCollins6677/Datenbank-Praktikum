import React, {useState } from 'react';

export function CDTable({ productList }){

    const [isChecked, setIsChecked] = useState(false);

    const filteredList = productList.filter((product) => {
        // Replace the condition with one that matches the specific type of product you want for Table B
        return product.künstler;
    });

    const handleButtonClick = () => {
        setIsChecked(!isChecked)
    }
    
    if(filteredList.length > 0){
        return (
            <h3> <div onClick={handleButtonClick}>CDs</div>
                {isChecked &&
                <table className="produktTable">
                    <thead>
                        <tr>
                            <th>Produktnummer</th>
                            <th>Titel</th>
                            <th>Rating</th>
                            <th>Rang</th>
                            <th>Bild</th>
                            <th>Erscheinungsjahr</th>
                            <th>Künstler</th>
                            <th>Label</th>
                            <th>Werke</th>
                            {/* Add specific attributes for Table B */}
                        </tr>
                    </thead>
                    <tbody>   
                    {filteredList.map( (product, index)=> (
                        <tr key ={index}>
                            <td>{product.prodnr}</td>
                            <td>{product.titel}</td>
                            <td>{product.rating}</td>
                            <td>{product.rang}</td>
                            <td>{product.bild}</td>
                            <td>{product.erscheinungjahr}</td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Künstler Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    {product.künstler.map( (künstler, künstlerIndex)=> (
                                        <tr key={künstlerIndex}>
                                            <td>{künstler}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Label Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    {product.label.map( (label, labelIndex)=> (
                                        <tr key={labelIndex}>
                                            <td>{label}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </td>
                            <td>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Werk Name</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    {product.werke.map( (werk, werkIndex)=> (
                                        <tr key={werkIndex}>
                                            <td>{werk}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </td>
                            {/* Add specific attributes for Table B */}
                        </tr>
                    ))}
                    </tbody>
                </table>
                }
            </h3>
        )
    } else {
        return ;
    }
}


export default CDTable;