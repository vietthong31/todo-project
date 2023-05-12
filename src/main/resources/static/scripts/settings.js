$(() => {
    $("aside a").filter((index, element) => {
        return location.href.includes($(element).attr("href"));
    }).addClass("selected")
})