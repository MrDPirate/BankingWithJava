# Banking With Java

A simple console banking simulator written in Java. It stores user and transaction data on disk (under the `db/` directory) and exposes a role-aware command line experience for bankers and customers.



## Key Features

- **First boot guard** – automatically provisions the very first banker if no user files are present.
- **Role-based menus** – bankers can register other users and manage clients, while customers see only their personal banking tools.
- **Banking operations** – deposit, withdraw, local transfer (between the same user’s accounts), and external transfer (to another user’s specified account type).
- **Password hashing & change flow** – credentials are stored as SHA‑256 hashes, and any authenticated user can change their password from the menu.
- **Detailed account summary** – view every stored attribute for the current account (except the password) including balances, card types, lock status, timestamps, and overdraft counters.
- **Transaction filters** – review all transactions, or narrow them down by date, custom ranges, transaction type, or account type directly from the CLI.

## Data & Storage

- User files live under `db/` (e.g. `db/Banker-Name-username`).
- Transaction ledgers live under `db/Trans/Transaction for-username`.
- All records are comma-separated for easy inspection and manual fixes when needed.

## Planning Board

Works are tracked on Trello: https://trello.com/b/vj02R3k2/banking-with-java
