window.addEventListener("load", () => {
    const navbarDiv = document.getElementById("nav-placeholder")

    navbarDiv.innerHTML = "<style>\n" +
        "  .bd-placeholder-img {\n" +
        "    font-size: 1.125rem;\n" +
        "    text-anchor: middle;\n" +
        "    -webkit-user-select: none;\n" +
        "    -moz-user-select: none;\n" +
        "    user-select: none;\n" +
        "  }\n" +
        "\n" +
        "  @media (min-width: 768px) {\n" +
        "    .bd-placeholder-img-lg {\n" +
        "      font-size: 3.5rem;\n" +
        "    }\n" +
        "  }\n" +
        "</style>\n" +
        "\n" +
        "<link href=\"/webApp/utils/all/navbar/navbar-top-fixed.css\" rel=\"stylesheet\">\n \n" +
        "\n" +
        "\n" +
        "<nav class=\"navbar navbar-expand-md navbar-dark fixed-top bg-dark\">\n" +
        "\n" +
        "  <div class=\"container-fluid\">\n" +
        "    <img class=\"mb-4\" src=\"/webApp/assets/brand/boutLogo.png\" width=\"105\" height=\"70\" href=\"/webApp/home.html\">\n" +
        "    <button class=\"navbar-toggler\" type=\"button\" data-bs-toggle=\"collapse\" data-bs-target=\"#navbarCollapse\" aria-controls=\"navbarCollapse\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n" +
        "      <span class=\"navbar-toggler-icon\"></span>\n" +
        "    </button>\n" +
        "    <div class=\"collapse navbar-collapse\" id=\"navbarCollapse\">\n" +
        "      <ul class=\"navbar-nav me-auto mb-2 mb-md-0\">\n" +
        "        <li class=\"nav-item dropdown\">\n" +
        "          <a class=\"nav-link dropdown-toggle\" id=\"dropdown01\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">Member</a>\n" +
        "          <ul class=\"dropdown-menu\" aria-labelledby=\"dropdown01\">\n" +
        "            <li><a class=\"dropdown-item\" href=\"/webApp/Member/account/account.html\">Account</a></li>\n" +
        "            <li><a class=\"dropdown-item\" href=\"/webApp/Member/reservation/reservation.html\">Reservation</a></li>\n" +
        "          </ul>\n" +
        "        </li>\n" +
        "        <li class=\"nav-item dropdown\">\n" +
        "          <a class=\"nav-link dropdown-toggle\" id=\"dropdown02\" data-bs-toggle=\"dropdown\" aria-expanded=\"false\">Manager</a>\n" +
        "          <ul class=\"dropdown-menu\" aria-labelledby=\"dropdown02\">\n" +
        "            <li><a class=\"dropdown-item\" href=\"#\">Account</a></li>\n" +
        "            <li><a class=\"dropdown-item\" href=\"#\">Reservation</a></li>\n" +
        "            <li><a class=\"dropdown-item\" href=\"#\">Time Window</a></li>\n" +
        "            <li><a class=\"dropdown-item\" href=\"#\">Boat</a></li>\n" +
        "            <li><a class=\"dropdown-item\" href=\"#\">Xml</a></li>\n" +
        "          </ul>\n" +
        "        </li>\n" +
        "\n" +
        "      </ul>\n" +
        "      <form class=\"d-flex\" action=\"logout\" method=\"post\">\n" +
        "        <button name=\"logout\" class=\"btn btn-outline-success\" type=\"submit\">logout</button>\n" +
        "      </form>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "</nav>\n" +
        "\n" +
        "<script src=\"/webApp/assets/dist/js/bootstrap.bundle.min.js\"></script>\n" +
        "\n";
});
