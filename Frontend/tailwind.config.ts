export default {
    darkMode: 'class',
    content: ['./src/**/*.{html,ts}'],
    theme: {
        extend: {
            screens: {
                'tablet': '850px', // Ein benutzerdefinierter Breakpoint für Tablets
                'md': '768px', // Standard-md beibehalten, falls nötig
            },
        },
    },
    plugins: [],
}
