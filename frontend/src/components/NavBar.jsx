const NavBar = (props) => {
  const { instance, instances, insChg } = props;
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-blue">
      <span className="navbar-brand" href="#">
        Integrations
      </span>
      <div
        className="collapse navbar-collapse"
        id="navbarSupportedContent"></div>
      <div className="dropdown">
        <button
          className="btn btn-light dropdown-toggle"
          type="button"
          id="dropdownMenu2"
          data-toggle="dropdown"
          aria-haspopup="true"
          aria-expanded="false">
          {instance}
        </button>

        <div className="dropdown-menu" aria-labelledby="dropdownMenu2">
          {instances.map((instance) => (
            <button
              key={instance}
              data-instance={instance}
              className="dropdown-item"
              type="button"
              onClick={insChg}>
              {instance}
            </button>
          ))}
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
