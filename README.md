## JBlock | POC Java Blockchain

This project demonstrates an internal Java blockchain that can store and maintain a chain of arbitrary objects.

1. A basic blockchain concept (based on public reference material) lives in `src/doc/concept`, with the `Block`, `Blockchain`, and `CryptoHash` classes.
   You can try the simple example by running `TestChain.java`.

   The project then evolved into a full **Blockchain** with network chains and a miner, available in the `com.R07r35cInc.blockchain` package.
   Implementation details are tracked in the [project issues](https://gitlab.com/cleberlecheta/java-blockchain/-/issues/?sort=created_date&state=all&first_page_size=20).

2. In that package, `Block` and `Blockchain` can store any kind of object: a document, a value, a simple domain object, a complex application object, or any other transaction type you need.
   Possible use cases considered:
   - an immutable user login table
   - a celebrated contract between parties

   The next major step was a **cryptocurrency** exploration.
   In the `com.R07r35cInc.cryptocurrency` package, a transaction currency was introduced to handle financial transfers specifically.

3. The key feature of that package is the **Wallet**, which securely stores account balances and transaction history that keep those balances consistent.
   Wallet keys use ECDSA digital signature verification, which makes the project suitable for realistic usage scenarios.

# Pipeline view

The project includes **74 tests** to help ensure quality and consistency for this scenario.
You can run all of them with:

```shell
mvn test
```

These tests are also grouped by responsibility in `.gitlab-ci.yml`. This is not a conventional CI/CD layout; it is meant to show the development flow across each responsibility block.

![Pipeline view](src/doc/pipeline-view.png)

# Maven repository

The project was initially developed with **Java 17** and is packaged and distributed with Maven so it can be used as a library in any Java project.

https://gitlab.com/cleberlecheta/java-blockchain/-/packages

# License

This project is licensed under the [Apache License, Version 2.0](LICENSE).
See the [NOTICE](NOTICE) file for copyright attribution.

# Source of truth and contributions

The **canonical repository** is on GitLab:

https://gitlab.com/cleberlecheta/java-blockchain

Please open **issues** and **merge requests** only on GitLab.
Any GitHub (or other) mirror is a **showcase only** and is not the official contribution workflow.

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.
