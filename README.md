# 🛒 SmartCart

SmartCart is a modern, responsive, and animated e-commerce web application built with **React**, **TypeScript**, **TailwindCSS**, **Framer Motion**, and **Lucide Icons**. It features a sleek UI, product catalog, cart functionality, authentication, and a built-in chatbot assistant.

---

## ✨ Features

- 🏠 **Home Page** with featured products and categories
- 🔍 **Category Filtering**
- 🛍️ **Product Details Page**
- 🛒 **Cart and Checkout**
- 🔐 **Authentication (Login/Signup)**
- 💬 **SmartCart AI Chatbot** for shopping help
- 📦 **Order Management (Protected Route)**
- 🌙 Responsive design with mobile support
- 🎨 Smooth animations using Framer Motion
- 🌐 Routing via React Router
- 🔧 Context API for global state management (Auth, Cart)

---

## 🧰 Tech Stack

| Layer         | Tech/Library                       |
|---------------|------------------------------------|
| Frontend      | React, TypeScript                  |
| Styling       | TailwindCSS                        |
| Animation     | Framer Motion                      |
| Icons         | Lucide React                       |
| State Mgmt    | Context API                        |
| Routing       | React Router DOM                   |
| Notifications | Sonner                             |
| Forms/Input   | Shadcn UI (button, card, input...) |
| API Handling  | Custom fetch utilities             |

---

## 📁 Folder Structure

src/
│
├── components/ # Reusable UI components (Navbar, Chatbot, Footer, etc.)
├── context/ # Auth and Cart context providers
├── pages/ # Route components (Home, ProductDetails, etc.)
├── utils/ # API helpers (e.g., fetchProductById)
├── types/ # TypeScript type definitions
├── App.tsx # Main app with routes
└── main.tsx # Entry point

---

## 🚀 Getting Started

### Prerequisites

- Node.js (v16+)
- npm or yarn

### 1. Clone the repo

```bash
git clone https://github.com/your-username/smartcart.git
cd smartcart
```
2. Install dependencies
```
npm install
# or
yarn install
```
3. Start the development server
```
npm run dev
# or
yarn dev
```
4. Build for production
```
npm run build
```
🔐 Environment Variables
If your project uses authentication or APIs, add the following in a .env file:
```
VITE_API_BASE_URL=https://your-backend-api.com
VITE_AUTH_SECRET=your-auth-token-key
```

🤖 AI Chatbot
SmartCart includes a floating AI Assistant powered by a local API function (sendChatMessage). You can hook it to OpenAI, Langchain, or any other LLM backend for a conversational shopping experience.

📦 Future Improvements
Payment gateway integration

Admin dashboard

Product reviews and ratings from users

Dark mode support

Infinite scroll and search functionality

🙌 Contributors
ANVESH SRIVASTAVA – Project Lead & Full Stack Developer

📄 License
This project is licensed under the MIT License.
