setTimeout(
  msg => {
    console.log(msg);
    setTimeout(msg => {
      console.log(msg);
    }, "bar");
  },
  1000,
  "foo"
);
