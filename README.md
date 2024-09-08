# Crypto Arbitrage Trading Bot with CoinDCX API

This project is a simple cryptocurrency trading bot built using Java and Spring Boot. It interacts with the CoinDCX API to fetch real-time market data (order books) and implements basic arbitrage strategies between three markets: SOL-INR, USDT-INR, and SOL-USDT.

The bot monitors these markets every 3 seconds, calculates potential arbitrage opportunities, and prints the results to the console.

## Features
- Real-time market data fetching from the CoinDCX public API.

- Implements two arbitrage strategies for potential trading opportunities:

- Convert SOL to INR, buy USDT, and then buy back SOL.

- Buy SOL from INR, convert it to USDT, and sell USDT for INR.

- Periodically logs highest bid and lowest ask prices for the monitored markets.

- REST API integration with CoinDCX for managing orders.
## Technology Stack
- Java

- Spring Boot

- REST APIs (CoinDCX Public and Private APIs)

- Jackson for JSON parsing.

- Apache HTTPClient for making authenticated API requests.

- Gson for serializing/deserializing JSON objects.

- dotenv-java for managing API keys and secrets securely.

## Dependencies
The following libraries are used in this project:

- Spring Boot for dependency injection and application structure.
- SLF4J with Logback for logging.

- Jackson for JSON parsing.
- RestTemplate (Spring) for making REST API calls.
- Gson for JSON conversion.
- Apache HTTPClient for sending HTTP POST requests to the CoinDCX API.
- Dotenv for environment variable management.
## Getting Started
### Prerequisites
- Java JDK 8 or higher installed on your machine.
- Maven installed to handle dependencies.
- A CoinDCX account with API keys. You will need to generate your API key and secret from your CoinDCX account.
## Installation
### Clone the repository:

```bash
git clone https://github.com/yourusername/crypto-arbitrage-bot.git
cd crypto-arbitrage-bot
```
### Set up the environment variables:

- Create a .env file in the root of the project.
- Add your CoinDCX API Key and API Secret:
```bash
API_KEY=your_coindcx_api_key
API_SECRET=your_coindcx_api_secret
```
### Install dependencies and build the project using Maven:

```bash
mvn clean install
```
### Run the application:

```bash
mvn spring-boot:run
```
### Running the Bot
Once the application starts, the bot will fetch the latest order book data from the CoinDCX public API every 3 seconds. It will log the highest bid and lowest ask for each market and print potential arbitrage opportunities.

## Code Structure
- Index.java: The main class that initializes the bot. It monitors the markets and logs the relevant data.
- DepthManager.java: Handles market data fetching from CoinDCX’s public API and extracts the order book information (bids and asks).
- CoinDCXApi.java: Interacts with the CoinDCX private API to place and cancel orders.
- TradingBotApplication.java: Entry point for the Spring Boot application.
## Arbitrage Logic
The bot implements two simple arbitrage strategies:

- SELL SOL FOR INR → BUY USDT FROM INR → BUY SOL FROM USDT
- BUY SOL FROM INR → SELL SOL FOR USDT → SELL USDT FOR INR
  
These strategies evaluate how much you can gain (or lose) by converting between different currencies using market prices from CoinDCX.

## Future Improvements
- Add automated order execution based on arbitrage conditions.
- Implement additional trading strategies.
- Add support for more markets and currency pairs.
- Introduce error handling and fallback mechanisms for API calls.
## Contributing
If you'd like to contribute to this project, feel free to fork the repository and submit a pull request. Contributions, issues, and feature requests are welcome!
