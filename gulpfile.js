var gulp = require("gulp");
var browserSync = require("browser-sync").create();
var sass = require("gulp-sass");
var autoprefixer = require("gulp-autoprefixer");
var plumber = require('gulp-plumber')
var minifyCSS = require('gulp-clean-css')
var rename = require('gulp-rename')
var sourcemaps = require('gulp-sourcemaps')
var uglify = require('gulp-uglify-es').default;

var autoprefixerOptions = {
    browsers: ["last 2 versions", "> 5%", "Firefox ESR"]
};

// Static Server + watching scss/html files
gulp.task("serve", ["sass"], function () {

    browserSync.init({
        server: {
            baseDir: "./Server Backend/",
            directory: true
        }
    });

    gulp.watch("./Server Backend/static/scss/*.scss", ["sass"]);
    gulp.watch("./Server Backend/static/js/*.js", ["js"])
    gulp.watch("./Server Backend/views/*.html").on("change", browserSync.reload())
});

// Compile sass into CSS & auto-inject into browsers
gulp.task("sass", function () {
    return gulp.src("./Server Backend/static/scss/*.scss")
        .pipe(sourcemaps.init())
        .pipe(plumber())
        .pipe(sass())
        .pipe(autoprefixer(autoprefixerOptions))
        .pipe(gulp.dest("./Server Backend/static/css"))
        .pipe(rename({ extname: '.min.css' }))
        .pipe(minifyCSS())
        .pipe(sourcemaps.write('maps'))
        .pipe(gulp.dest("./Server Backend/static/min/css"))
        .pipe(browserSync.stream());
});

gulp.task("js", function() {
    return gulp.src("./Server Backend/static/js/*.js")
        .pipe(sourcemaps.init())
        .pipe(plumber())
        .pipe(uglify())
        .pipe(rename({ extname: '.min.js' }))
        .pipe(sourcemaps.write('maps'))
        .pipe(gulp.dest("./Server Backend/static/min/js")) // save .min.js
        .pipe(browserSync.reload());
})

gulp.task("default", ["serve"]);