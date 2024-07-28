## JBlock | POC Java Blockchain

The project aims to prove the availability of an internal Java Blockchain able to store and maintain any chain of objects

1. I defined a basic blockchain concept based in Internet research you can find it in `src/doc/concept` within classes Block, Blockchain and CryptoHash.
You can check this simple example by running:
``TestChain.java``

Then I evolute the project to a full **Blockchain** using the network chains and miner as you can find in package `com.R07r35cInc.blockchain`

2. In this package I prepared the Block and Blockchain to store any kind of objects. We can store a Document, a Value, 
a simple object, a complex object of you domain application any other kind of Transaction you may want.
Possible usages considered
- an immutable login user table
- a celebrated contract between parts

The last but not least development was the develop of a **cryptocurrency**.
In package `com.R07r35cInc.cryptocurrency` I explored the creation of a
transaction currency to handle specifically financial transactions.

3. The key feature of this package is the **Wallet**, able to securely store
the balance of accounts and transaction history that guarantee this correct balance.
The wallet keys has ECDSA Digital Signature Verification making this project able to real use scenarios.

# Pipeline view
The project has _74 test implemented_ to ensure both quality and consistence of this complex scenario.
You can run all at once by just running `mvn test`.
I also separated these tests in block of responsibilities in `gitlab-cy.yml`. As it`s not
an usual CI/CD pipeline implementation it mean to show the development flow within each block of responsibilities.
![Pipeline view](src/doc/pipeline-view.png)

# Maven repository
The project was initial developed with **Java 17** and packed and distributed by maven for usage
as a library in any project that can handle Java. 

https://gitlab.com/cleberlecheta/java-blockchain/-/packages