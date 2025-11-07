/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,jsx,ts,tsx}', // For Next.js App Router
    './pages/**/*.{js,jsx,ts,tsx}', // If your pages are here
    './components/**/*.{js,jsx,ts,tsx}', // If your components are here
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
