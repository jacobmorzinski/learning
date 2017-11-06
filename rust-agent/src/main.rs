use std::process::Command;
use std::env;

fn main() {
    let args = env::args();
    if args.len() == 1 {
        let mut child = Command::new("date.exe")
                            .arg("-h")
                            .spawn()
                            .expect("error starting child");
        println!("child: {}", child.id());
        let ecode = child.wait()
                        .expect("error wait on child");
        println!("child ecode: {}", ecode);
    } else {
        println!("I'm a daemon!");
    }
}
