/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}", // Paths to all template files
  ],
  theme: {
    extend: {
      // You can extend the default Tailwind theme here
      // For example, custom colors, fonts, spacing, etc.
      // colors: {
      //   'brand-blue': '#007bff',
      //   'brand-green': '#28a745',
      // },
    },
  },
  plugins: [
    // You can add Tailwind plugins here
    // require('@tailwindcss/forms'),
    // require('@tailwindcss/typography'),
  ],
}
