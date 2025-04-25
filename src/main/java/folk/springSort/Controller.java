package folk.springSort;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

@RestController
public class Controller {

    @PostMapping(value = "/file",
            consumes = "multipart/form-data")
    public String getMinNum(@RequestParam("file") MultipartFile file,
                            @RequestParam("n") int n) throws IOException {

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    int numFromCell = (int) cell.getNumericCellValue();
                    if (priorityQueue.size() < n) {
                        priorityQueue.add(numFromCell);
                    }
                    if (numFromCell < priorityQueue.peek()) {
                        priorityQueue.poll();
                        priorityQueue.add(numFromCell);
                    }

                }
            }
        }

        return String.valueOf(priorityQueue.peek());
    }
}
