/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './app/**/*.{js,jsx,ts,tsx}', // Next.js App Router için
    './pages/**/*.{js,jsx,ts,tsx}', // Eğer sayfalarınız burada ise
    './components/**/*.{js,jsx,ts,tsx}', // Eğer componentler burada ise
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
