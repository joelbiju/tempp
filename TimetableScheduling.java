import java.util.ArrayList;
import java.util.List;

public class TimetableScheduling {
    private static final int MAX_DAYS = 5;
    private static final int MAX_HOURS = 7;
    private static final int TIME_QUANTUM = 1;

    static class Subject {
        String name;
        int burstTime;
        int weight;

        Subject(String name, int burstTime, int weight) {
            this.name = name;
            this.burstTime = burstTime;
            this.weight = weight;
        }
    }

    static class Faculty {
        String name;
        List<Subject> subjects;

        Faculty(String name) {
            this.name = name;
            this.subjects = new ArrayList<>();
        }
    }

    static class Classroom {
        String name;
        List<Subject> subjects;

        Classroom(String name) {
            this.name = name;
            this.subjects = new ArrayList<>();
        }
    }

    public static void generateTimetable(List<Faculty> faculties, Classroom classroom, String[][] timetable) {
        int currentDay = 0;
        int currentHour = 0;

        List<Subject> subjectQueue = new ArrayList<>();
        for (Subject subject : classroom.subjects) {
            for (int i = 0; i < subject.weight; i++) {
                subjectQueue.add(subject);
            }
        }

        int queueSize = subjectQueue.size();
        int queueIndex = 0;

        while (currentDay < MAX_DAYS) {
            Subject currentSubject = subjectQueue.get(queueIndex);

            if (currentSubject.burstTime > 0 && isFacultyAvailable(faculties, currentSubject.name, currentDay, currentHour)) {
                timetable[currentDay][currentHour] = currentSubject.name;
                currentSubject.burstTime--;
            }

            currentHour++;
            if (currentHour >= MAX_HOURS) {
                currentHour = 0;
                currentDay++;
            }

            queueIndex = (queueIndex + 1) % queueSize;
        }
    }

    public static boolean isFacultyAvailable(List<Faculty> faculties, String subjectName, int day, int hour) {
        for (Faculty faculty : faculties) {
            for (Subject subject : faculty.subjects) {
                if (subject.name.equals(subjectName) && subject.burstTime > 0) {
                    subject.burstTime--;
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("Faculty 1") {{
            subjects.add(new Subject("A", 3, 1));
            subjects.add(new Subject("B", 4, 1));
        }});
        faculties.add(new Faculty("Faculty 2") {{
            subjects.add(new Subject("C", 4, 1));
            subjects.add(new Subject("D", 3, 1));
        }});
        faculties.add(new Faculty("Faculty 3") {{
            subjects.add(new Subject("E", 4, 1));
            subjects.add(new Subject("F", 1, 1));
            subjects.add(new Subject("G", 1, 1));
        }});

        List<Classroom> classrooms = new ArrayList<>();
        classrooms.add(new Classroom("Classroom 1") {{
            subjects.add(new Subject("A", 3, 1));
            subjects.add(new Subject("C", 4, 1));
            subjects.add(new Subject("E", 4, 1));
        }});
        classrooms.add(new Classroom("Classroom 2") {{
            subjects.add(new Subject("B", 4, 1));
            subjects.add(new Subject("D", 3, 1));
            subjects.add(new Subject("F", 1, 1));
            subjects.add(new Subject("G", 1, 1));
        }});

        String[][][] timetables = new String[classrooms.size()][MAX_DAYS][MAX_HOURS];

        for (int i = 0; i < classrooms.size(); i++) {
            Classroom classroom = classrooms.get(i);
            String[][] timetable = timetables[i];

            generateTimetable(faculties, classroom, timetable);

            System.out.println("Timetable for " + classroom.name + ":");
            for (String[] row : timetable) {
                for (String subject : row) {
                    System.out.print(subject + "\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
