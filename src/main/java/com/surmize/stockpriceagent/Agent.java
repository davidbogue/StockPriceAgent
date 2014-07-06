package com.surmize.stockpriceagent;

import com.surmize.dao.StockPriceDAO;
import com.surmize.dao.StockSymbolDAO;
import com.surmize.models.StockSymbol;
import com.surmize.stockpricecrawler.StockPrice;
import com.surmize.stockpricecrawler.StockPriceService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Agent {

    private final StockSymbolDAO symbolDao = new StockSymbolDAO();
    private final StockPriceDAO stockDao = new StockPriceDAO();
    private final StockPriceService stockService = new StockPriceService();
            
    public static void main(String args[]) throws SQLException{
        Agent a = new Agent();
        a.collectStockPrices();
    }
    
    public void collectStockPrices() throws SQLException{
        List<String> symbols =  getSymbolStrings( symbolDao.getActiveStockSymbols() );
        List<StockPrice> stockPrices = stockService.getStockPriceData(symbols);
        for (StockPrice stockPrice : stockPrices) {
            stockDao.insertEntity( getSurmizeStockPrice(stockPrice) );
        }
    }
    
    private List<String> getSymbolStrings (List<StockSymbol> symbols){
        List<String> symbolStrings = new ArrayList<>();
        for (StockSymbol symbol : symbols) {
            symbolStrings.add(symbol.symbol);
        }
        return symbolStrings;
    }
    
    private com.surmize.models.StockPrice getSurmizeStockPrice(StockPrice sp){
        com.surmize.models.StockPrice price = new com.surmize.models.StockPrice();
        price.dayHigh = sp.dayHigh;
        price.dayLow = sp.dayLow;
        price.peRatio = sp.peRatio;
        price.pegRatio = sp.pegRatio;
        price.price = sp.price;
        price.stockSymbol = sp.stockSymbol;
        price.tradeTime = sp.tradeTime;
        return price;
    }
}
